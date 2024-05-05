package cn.chengzhiya.mhdfbotbukkithook.task;

import cn.chengzhiya.mhdfbotbukkithook.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;

public final class SendMessage extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!ifPlayerDataExist(player.getName())) {
                String[] Title = Objects.requireNonNull(main.main.getConfig().getString("Messages.SendMessage")).split("\\|");
                String[] Sound = Objects.requireNonNull(main.main.getConfig().getString("Sound.SendMessage")).split("\\|");

                Bukkit.getScheduler().runTask(main.main, () -> {
                    player.sendTitle(Title[0], Title[1], Integer.parseInt(Title[2]), Integer.parseInt(Title[3]), Integer.parseInt(Title[5]));
                    player.playSound(player, org.bukkit.Sound.valueOf(Sound[0]), Float.parseFloat(Sound[1]), Float.parseFloat(Sound[2]));
                });
            }
        }
    }
}
