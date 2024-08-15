package cn.ChengZhiYa.MHDFBot.api;

import cn.ChengZhiYa.MHDFBot.api.manager.CommandExecutor;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.api.manager.Plugin;
import cn.ChengZhiYa.MHDFBot.entity.plugin.PluginInfo;
import cn.ChengZhiYa.MHDFBot.util.LogUtil;
import lombok.Data;

@Data
public abstract class MHDFBotPlugin implements Plugin {
    private PluginInfo pluginInfo;

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void colorLog(String message) {
        LogUtil.colorLog(message);
    }

    public void registerCommand(CommandExecutor commandExecute, String... commands) {
        MHDFBot.registerCommand(getPluginInfo(), commandExecute, commands);
    }

    public void registerCommand(String description, CommandExecutor commandExecute, String... commands) {
        MHDFBot.registerCommand(getPluginInfo(), description, commandExecute, commands);
    }

    public void registerCommand(String description, String usage, CommandExecutor commandExecute, String... commands) {
        MHDFBot.registerCommand(getPluginInfo(), description, usage, commandExecute, commands);
    }

    public void registerListener(Listener listener) {
        MHDFBot.registerListener(listener);
    }
}
