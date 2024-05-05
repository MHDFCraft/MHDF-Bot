package cn.chengzhiya.mhdfbotbungeecordhook.listener;

import cn.chengzhiya.mhdfbotbungeecordhook.util.Util;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;

public final class ServerConnect implements Listener {
    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (!event.isCancelled()) {
            if (!Util.getConfig().getStringList("AllowJoinServerList").contains(event.getTarget().getName())) {
                if (!ifPlayerDataExist(event.getPlayer().getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
