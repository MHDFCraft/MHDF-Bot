package cn.ChengZhiYa.MHDFBot.command;

import cn.ChengZhiYa.MHDFBot.api.MHDFBot;
import cn.ChengZhiYa.MHDFBot.command.main.Help;
import cn.ChengZhiYa.MHDFBot.command.main.Plugins;

public final class CommandRegister {
    public static void registerCommands() {
        MHDFBot.registerCommand(null, "查询插件列表", new Plugins(), "pl", "plugins");
        MHDFBot.registerCommand(null, "查询命令帮助", new Help(), "help");
    }
}
