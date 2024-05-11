package cn.chengzhiya.mhdfbotbukkithook.listener;

import cn.chengzhiya.mhdfbotbukkithook.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.runAction;

public final class PlayerCommandPreprocess implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!ifPlayerDataExist(event.getPlayer().getName())) {
            event.setCancelled(!(main.main.getConfig().getStringList("AllowUseCommandList").contains(event.getMessage().split(" ")[0])));
            if (event.isCancelled()) {
                runAction(event.getPlayer(), main.main.getConfig().getStringList("Actions.AntiUseCommand"));
            }
        }
    }
}
