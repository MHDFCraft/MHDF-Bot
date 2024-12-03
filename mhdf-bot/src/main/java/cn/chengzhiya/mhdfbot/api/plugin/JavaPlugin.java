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
        FileUtil.saveResource(new File(this.getDataFolder(),filePath).getPath(), resourcePath, replace);
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
