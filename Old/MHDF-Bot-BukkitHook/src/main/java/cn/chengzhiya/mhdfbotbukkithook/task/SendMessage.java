package cn.ChengZhiYa.mhdfbotbukkithook.task;

import cn.ChengZhiYa.mhdfbotbukkithook.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.ChengZhiYa.mhdfbotbukkithook.util.Util.runAction;

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
