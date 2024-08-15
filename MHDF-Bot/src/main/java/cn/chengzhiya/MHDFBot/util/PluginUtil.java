package cn.ChengZhiYa.MHDFBot.util;

import cn.ChengZhiYa.MHDFBot.MHDFBot;
import cn.ChengZhiYa.MHDFBot.api.MHDFBotPlugin;
import cn.ChengZhiYa.MHDFBot.api.enums.PluginStatus;
import cn.ChengZhiYa.MHDFBot.entity.YamlConfiguration;
import cn.ChengZhiYa.MHDFBot.entity.plugin.PluginInfo;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static cn.ChengZhiYa.MHDFBot.util.LogUtil.colorLog;

public final class PluginUtil {
    @Getter
    static HashMap<PluginInfo, PluginStatus> pluginHashMap = new HashMap<>();

    public static void loadPlugins() throws IOException {
        File pluginFolder = new File("./plugins");
        File[] plugins = pluginFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (plugins != null) {
            for (File plugin : plugins) {
                try (JarFile jarFile = new JarFile(plugin)) {
                    JarEntry jarEntry = jarFile.getJarEntry("plugin.yml");
                    if (jarEntry != null) {
                        YamlConfiguration pluginInfoData = YamlConfiguration.loadConfiguration(jarFile.getInputStream(jarEntry));
                        PluginInfo pluginInfo = new PluginInfo(
                                pluginInfoData.getString("name"),
                                pluginInfoData.getString("version"),
                                pluginInfoData.getStringList("author"),
                                pluginInfoData.getString("main")
                        );
                        try {
                            URL[] urls = {plugin.toURI().toURL()};
                            URLClassLoader classLoader = new URLClassLoader(urls, MHDFBot.class.getClassLoader());
                            Class<?> clazz = classLoader.loadClass(pluginInfo.getMain());
                            MHDFBotPlugin mhdfBotPlugin = (MHDFBotPlugin) clazz.getDeclaredConstructor().newInstance();
                            mhdfBotPlugin.setPluginInfo(pluginInfo);
                            mhdfBotPlugin.onEnable();
                            getPluginHashMap().put(pluginInfo, PluginStatus.Load_Done);
                        } catch (Exception e) {
                            getPluginHashMap().put(pluginInfo, PluginStatus.Load_Error);
                            e.printStackTrace();
                        }
                    } else {
                        colorLog("&c{}不是一个有效的插件!", plugin.getName());
                    }
                }
            }
        }
    }
}
