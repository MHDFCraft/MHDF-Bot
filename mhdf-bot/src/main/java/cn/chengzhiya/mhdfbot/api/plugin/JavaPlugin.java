package cn.chengzhiya.mhdfbot.api.plugin;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.entity.plugin.Command;
import cn.chengzhiya.mhdfbot.api.entity.plugin.PluginInfo;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import lombok.Data;
import org.apache.logging.log4j.Logger;

@Data
@SuppressWarnings("unused")
public abstract class JavaPlugin implements Plugin {
    private PluginInfo pluginInfo;

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
}
