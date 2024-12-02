package cn.chengzhiya.mhdfbot.api.manager;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.entity.config.YamlConfiguration;
import cn.chengzhiya.mhdfbot.api.entity.plugin.Command;
import cn.chengzhiya.mhdfbot.api.entity.plugin.PluginInfo;
import cn.chengzhiya.mhdfbot.api.enums.plugin.PluginStatus;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbot.api.plugin.JavaPlugin;
import cn.chengzhiya.mhdfbot.api.util.FileUtil;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Getter
@SuppressWarnings({"CallToPrintStackTrace", "unchecked", "unused"})
public final class PluginManager {
    private final Path pluginFolder = Paths.get("./plugins");
    private final HashMap<String, PluginInfo> pluginHashMap = new HashMap<>();

    /**
     * 获取指定插件名称的插件实例
     *
     * @param pluginName 插件名称
     * @return 插件实例
     */
    public PluginInfo getPlugin(String pluginName) {
        return pluginHashMap.get(pluginName);
    }

    /**
     * 获取插件实例列表
     *
     * @return 插件实例列表
     */
    public List<PluginInfo> getPluginList() {
        return new ArrayList<>(pluginHashMap.values());
    }

    /**
     * 加载插件目录下所有插件
     */
    public void loadPlugins() throws IOException {
        FileUtil.createFolder(pluginFolder.toFile());
        try (Stream<Path> paths = Files.list(pluginFolder)) {
            paths.filter(path -> path.toString().endsWith(".jar")).forEach(this::loadPlugin);
        }
    }

    /**
     * 加载指定插件文件
     *
     * @param pluginPath 插件文件路径
     */
    public void loadPlugin(Path pluginPath) {
        try (JarFile jarFile = new JarFile(pluginPath.toFile())) {
            // 获取插件描述文件
            JarEntry pluginInfoFile = jarFile.getJarEntry("plugin.yml");
            if (pluginInfoFile == null) {
                MHDFBot.getLogger().error("{}不是一个有效的插件!", pluginPath.getFileName());
                return;
            }

            // 处理插件描述文件
            YamlConfiguration pluginInfoData = YamlConfiguration.loadConfiguration(jarFile.getInputStream(pluginInfoFile));
            PluginInfo pluginInfo = new PluginInfo(
                    pluginInfoData.getString("name"),
                    pluginInfoData.getString("version"),
                    pluginInfoData.getString("main"),
                    pluginInfoData.getStringList("authoendrs")
            );

            MHDFBot.getLogger().info("插件{}({})加载中", pluginInfo.getName(), pluginInfo.getVersion());

            // 加载插件
            try {
                URL[] urls = {pluginPath.toUri().toURL()};
                URLClassLoader classLoader = new URLClassLoader(urls, MHDFBot.class.getClassLoader());
                Thread.currentThread().setContextClassLoader(classLoader);
                Class<?> clazz = classLoader.loadClass(pluginInfo.getMain());
                JavaPlugin javaPlugin = (JavaPlugin) clazz.getDeclaredConstructor().newInstance();
                javaPlugin.setPluginInfo(pluginInfo);
                pluginInfo.setPlugin(javaPlugin);
                pluginInfo.setJarFile(jarFile);

                javaPlugin.onEnable();

                pluginInfo.setPluginStatus(PluginStatus.Load_Done);
            } catch (Exception e) {
                pluginInfo.setPluginStatus(PluginStatus.Load_Error);
                e.printStackTrace();
            }

            getPluginHashMap().put(pluginInfo.getName(), pluginInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 卸载所有插件
     */
    public void unloadPlugins() {
        for (PluginInfo pluginInfo : getPluginHashMap().values()) {
            unloadPlugin(pluginInfo);
        }
    }

    /**
     * 卸载指定插件
     *
     * @param pluginInfo 插件信息
     */
    public void unloadPlugin(PluginInfo pluginInfo) {
        if (pluginInfo == null) {
            return;
        }
        getPluginHashMap().remove(pluginInfo.getName());

        // 取消注册监听器
        {
            HashMap<Listener, PluginInfo> hashMap = (HashMap<Listener, PluginInfo>) MHDFBot.getListenerManager().getListenerHashMap().clone();
            hashMap.forEach((listener, pluginInfo2) -> {
                if (pluginInfo == pluginInfo2) {
                    hashMap.remove(listener);
                }
            });
        }

        // 取消注册命令
        {
            HashMap<String, Command> hashMap = (HashMap<String, Command>) MHDFBot.getCommandManager().getCommandHashMap().clone();
            hashMap.forEach((s, command) -> {
                if (pluginInfo == command.getPluginInfo()) {
                    hashMap.remove(s);
                }
            });
        }

        // 卸载插件
        pluginInfo.getPlugin().onDisable();

        try {
            pluginInfo.getJarFile().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重载指定插件
     *
     * @param pluginName 插件名称
     */
    public void reloadPlugin(String pluginName) {
        PluginInfo pluginInfo = getPlugin(pluginName);
        if (pluginInfo != null) {
            Path pluginPath = Path.of(getPlugin(pluginName).getJarFile().getName());
            unloadPlugin(pluginInfo);
            loadPlugin(pluginPath);
        }
    }
}
