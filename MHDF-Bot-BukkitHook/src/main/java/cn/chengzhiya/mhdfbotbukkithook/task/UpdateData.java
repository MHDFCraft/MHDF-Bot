package cn.chengzhiya.mhdfbotbukkithook.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.updatePlayerData;

public final class UpdateData extends BukkitRunnable {
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ifPlayerDataExist(player.getName())) {
                updatePlayerData(player.getName());
            }
        }
    }
}
