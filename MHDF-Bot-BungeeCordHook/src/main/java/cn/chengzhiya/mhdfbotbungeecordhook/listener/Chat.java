package cn.chengzhiya.mhdfbotbungeecordhook.listener;

import cn.chengzhiya.mhdfbotbungeecordhook.util.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;

public final class Chat implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        if (!event.isCancelled()) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            if (!ifPlayerDataExist(player.getName())) {
                if (event.isCommand() || event.isProxyCommand()) {
                    event.setCancelled(Util.getConfig().getStringList("AllowUseCommandList").contains(event.getMessage().split(" ")[0]));
                    player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Util.getConfig().getString("Messages.AntiUseCommand"))));
                } else {
                    event.setCancelled(Util.getConfig().getBoolean("AllowChat"));
                    player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', Util.getConfig().getString("Messages.AntiChat"))));
                }
            }
        }
    }
}
