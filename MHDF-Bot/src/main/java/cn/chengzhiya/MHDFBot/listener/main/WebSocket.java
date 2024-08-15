package cn.ChengZhiYa.MHDFBot.listener.main;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.minecraft.WebSocketEvent;

import static cn.ChengZhiYa.MHDFBot.util.LogUtil.colorLog;

public final class WebSocket implements Listener {
    @EventHandler
    public void onWebSocket(WebSocketEvent event) {
        colorLog(event.getData().toJSONString());
    }
}
