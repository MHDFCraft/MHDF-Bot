package cn.ChengZhiYa.MHDFBotBungeeCordHook.listener;

import cn.ChengZhiYa.MHDFBotBungeeCordHook.event.WebSocketEvent;
import cn.ChengZhiYa.MHDFBotBungeeCordHook.main;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class WebSocket implements Listener {
    @EventHandler
    public void onWebSocket(WebSocketEvent event) {
        main.instance.getLogger().info(event.getData().toJSONString());
    }
}
