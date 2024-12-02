package cn.chengzhiya.mhdfbot.command;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.command.CommandExecutor;
import cn.chengzhiya.mhdfbot.api.entity.plugin.Command;

import java.util.List;

public final class Help implements CommandExecutor {
    @Override
    public void onCommand(String command, String[] args) {
        List<Command> commandList = MHDFBot.getCommandManager().getCommandList();

        int page = args.length >= 1 ? Integer.parseInt(args[0]) : 1;
        int maxPage = this.ceilDiv(commandList.size(), 5);

        if (page > maxPage) {
            return;
        }

        this.printCommandList(commandList, page, maxPage);
    }

    /**
     * 打印命令列表
     *
     * @param commandList 命令列表
     * @param page        页数
     * @param maxPage     最大页数
     */
    private void printCommandList(List<Command> commandList, int page, int maxPage) {
        MHDFBot.getLogger().info("----------=梦之机器人框架 | 帮助=----------");
        for (int i = (page - 1) * 5; i < page * 5; i++) {
            if (commandList.size() <= i) {
                break;
            }
            Command command = commandList.get(i);
            MHDFBot.getLogger().info(
                    "{} - {} | 来自: {}({})",
                    command.getUsage() != null ? command.getUsage() : command.getCommand(),
                    command.getDescription(),
                    command.getPluginInfo().getName(),
                    command.getPluginInfo().getVersion()
            );
        }
        MHDFBot.getLogger().info("页数: {}/{}", page, maxPage);
        MHDFBot.getLogger().info("----------=梦之机器人框架 | 帮助=----------");
    }

    /**
     * 进一除法
     *
     * @param numerator   被除数
     * @param denominator 除数
     * @return 结果
     */
    private int ceilDiv(int numerator, int denominator) {
        return (numerator + denominator - 1) / denominator;
    }
}
