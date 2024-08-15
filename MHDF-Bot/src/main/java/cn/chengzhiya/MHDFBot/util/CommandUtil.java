package cn.ChengZhiYa.MHDFBot.util;

import cn.ChengZhiYa.MHDFBot.api.manager.CommandExecutor;
import cn.ChengZhiYa.MHDFBot.entity.plugin.Command;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static cn.ChengZhiYa.MHDFBot.util.LogUtil.colorLog;

public final class CommandUtil {
    @Getter
    static Map<String, Command> commandHashMap = new HashMap<>();

    public static void executeCommand(String commandString) {
        String[] parts = commandString.split(" ");
        String command = parts[0];
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);
        if (commandHashMap.get(command) != null) {
            CommandExecutor commandExecutor = commandHashMap.get(command).getCommandExecute();
            commandExecutor.run(command, args);
        } else {
            colorLog("&c{}命令不存在!", command);
        }
    }
}
