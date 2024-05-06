package cn.chengzhiya.mhdfbotbukkithook.task;

import cn.chengzhiya.mhdfbotbukkithook.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class SendMessage extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!ifPlayerDataExist(player.getName())) {
                String[] Title = Objects.requireNonNull(main.main.getConfig().getString("Messages.NoBind")).split("\\|");
                String[] Sound = Objects.requireNonNull(main.main.getConfig().getString("Sound.NoBind")).split("\\|");

                Bukkit.getScheduler().runTask(main.main, () -> {
                    player.sendTitle(ChatColor(Title[0]), ChatColor(Title[1]), Integer.parseInt(Title[2]), Integer.parseInt(Title[3]), Integer.parseInt(Title[4]));
                    player.playSound(player, org.bukkit.Sound.valueOf(Sound[0]), Float.parseFloat(Sound[1]), Float.parseFloat(Sound[2]));
                });
            }
        }
    }
}
