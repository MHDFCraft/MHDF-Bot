package cn.ChengZhiYa.MHDFBot.entity.plugin;

import cn.ChengZhiYa.MHDFBot.api.manager.CommandExecutor;
import lombok.Data;

@Data
public final class Command {
    PluginInfo pluginInfo;
    CommandExecutor commandExecute;
    String description;
    String usage;

    public Command(PluginInfo pluginInfo, CommandExecutor commandExecute) {
        this.pluginInfo = pluginInfo;
        this.commandExecute = commandExecute;
    }

    public Command(PluginInfo pluginInfo, CommandExecutor commandExecute, String description) {
        this.pluginInfo = pluginInfo;
        this.commandExecute = commandExecute;
        this.description = description;
    }

    public Command(PluginInfo pluginInfo, CommandExecutor commandExecute, String description, String usage) {
        this.pluginInfo = pluginInfo;
        this.commandExecute = commandExecute;
        this.description = description;
        this.usage = usage;
    }
}
