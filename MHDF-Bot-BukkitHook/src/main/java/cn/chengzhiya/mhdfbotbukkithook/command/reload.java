package cn.chengzhiya.mhdfbotbukkithook.command;

import cn.chengzhiya.mhdfbotbukkithook.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        main.main.reloadConfig();
        sender.sendMessage(ChatColor(main.main.getConfig().getString("Messages.ReloadDone")));
        return false;
    }
}
