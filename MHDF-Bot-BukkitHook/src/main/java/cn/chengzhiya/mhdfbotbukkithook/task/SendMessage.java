package cn.chengzhiya.mhdfbotbukkithook.task;

import cn.chengzhiya.mhdfbotbukkithook.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.runAction;

public final class SendMessage extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!ifPlayerDataExist(player.getName())) {
                runAction(player, main.main.getConfig().getStringList("Actions.NotBind"));
            }
        }
    }
}
