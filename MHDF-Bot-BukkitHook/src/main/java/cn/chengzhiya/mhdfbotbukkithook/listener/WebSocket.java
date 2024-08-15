package cn.ChengZhiYa.MHDFBotBukkitHook.listener;

import cn.ChengZhiYa.MHDFBotBukkitHook.event.WebSocketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static cn.ChengZhiYa.MHDFBotBukkitHook.util.MessageUtil.colorLog;

public final class WebSocket implements Listener {
    @EventHandler
    public void onWebSocket(WebSocketEvent event) {
        colorLog(event.getData().toJSONString());
    }
}
