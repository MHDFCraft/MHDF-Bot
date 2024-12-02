package cn.chengzhiya.mhdfbot.api.manager;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.entity.plugin.Command;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@SuppressWarnings("unused")
public final class CommandManager {
    private final HashMap<String, Command> commandHashMap = new HashMap<>();

    /**
     * 注册命令实例
     *
     * @param command 命令实例
     */
    public void registerCommand(Command command) {
        getCommandHashMap().put(command.getCommand(), command);
    }

    /**
     * 获取指定命令的命令实例
     *
     * @param command 命令
     * @return 命令实例
     */
    public Command getCommand(String command) {
        return getCommandHashMap().get(command);
    }

    /**
     * 获取命令实例列表
     *
     * @return 命令实例列表
     */
    public List<Command> getCommandList() {
        return new ArrayList<>(getCommandHashMap().values());
    }

    /**
     * 执行指定命令
     *
     * @param commandString 完整命令
     */
    public void executeCommand(String commandString) {
        String[] parts = commandString.split(" ");

        String command = parts[0];
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);

        if (getCommand(parts[0]) == null) {
            MHDFBot.getLogger().error("找不到这个命令");
            return;
        }

        getCommand(parts[0]).getExecutor().onCommand(command, args);
    }

    /**
     * 补全指定命令
     *
     * @param input 当前输入内容
     */
    public List<String> tabComplete(String input) {
        long length = input.chars().filter(c -> c == ' ').count();

        if (length == 0) {
            return getCommandHashMap().keySet().stream().toList();
        }

        String[] parts = input.split(" ");

        Command command = getCommand(parts[0]);

        if (command == null) {
            return new ArrayList<>();
        }

        if (command.getTabCompleter() == null) {
            return new ArrayList<>();
        }

        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, parts.length - 1);

        return command.getTabCompleter().onTabComplete(parts[0], args);
    }
}
