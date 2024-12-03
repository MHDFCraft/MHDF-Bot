package cn.chengzhiya.mhdfbot.api.plugin;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.entity.config.YamlConfiguration;
import cn.chengzhiya.mhdfbot.api.entity.plugin.Command;
import cn.chengzhiya.mhdfbot.api.entity.plugin.PluginInfo;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbot.api.util.FileUtil;
import lombok.Data;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Data
@SuppressWarnings("unused")
public abstract class JavaPlugin implements Plugin {
    private PluginInfo pluginInfo;
    private YamlConfiguration config;

    /**
     * 获取日志实例
     *
     * @return 日志实例
     */
    public Logger getLogger() {
        return MHDFBot.getLogger(this.pluginInfo.getName());
    }

    /**
     * 注册命令实例
     *
     * @param command 命令实例
     */
    public void registerCommand(Command command) {
        command.plugin(this.pluginInfo);
        MHDFBot.getCommandManager().registerCommand(command);
    }

    /**
     * 注册指定监听器实例
     *
     * @param listener 监听器实例
     */
    public void registerListener(Listener listener) {
        MHDFBot.getListenerManager().registerListener(this.pluginInfo, listener);
    }

    /**
     * 保存默认配置文件
     */
    public void saveDefaultConfig() {
        FileUtil.createFolder(this.getDataFolder());
        this.saveResource("config.yml", "config.yml", false);
    }

    /**
     * 重载配置文件
     */
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
    }

    /**
     * 保存资源
     *
     * @param filePath     保存目录
     * @param resourcePath 资源目录
     * @param replace      替换文件
     */
    public void saveResource(String filePath, String resourcePath, boolean replace) {
        File file = new File(this.getDataFolder(), filePath);
        if (file.exists() && !replace) {
            return;
        }

        URL url = this.getClass().getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new RuntimeException("找不到资源: " + resourcePath);
        }

        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);

            try (InputStream in = url.openStream()) {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    if (in == null) {
                        throw new RuntimeException("读取资源 " + resourcePath + " 的时候发生了错误");
                    }

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取插件数据目录实例
     *
     * @return 数据目录实例
     */
    public File getDataFolder() {
        return new File("./plugins/" + this.pluginInfo.getName());
    }
}
