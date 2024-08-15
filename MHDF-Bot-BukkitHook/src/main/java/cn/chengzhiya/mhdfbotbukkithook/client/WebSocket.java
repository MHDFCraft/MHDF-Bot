package cn.ChengZhiYa.MHDFBotBukkitHook.client;

import cn.ChengZhiYa.MHDFBotBukkitHook.event.WebSocketEvent;
import cn.ChengZhiYa.MHDFBotBukkitHook.main;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBukkitHook.util.MessageUtil.colorLog;

@ClientEndpoint
public final class WebSocket {
    private static final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    public static Session session;
    @Getter
    static HashMap<String, JSONObject> echoHashMap = new HashMap<>();

    public static void connectWebSocketServer() {
        try {
            container.connectToServer(new WebSocket(), new URI(Objects.requireNonNull(main.instance.getConfig().getString("WebSocketSettings.Host"))));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            colorLog("&c无法正常连接至websocket服务端!");
            e.printStackTrace();
            Bukkit.getScheduler().runTaskLaterAsynchronously(main.instance,WebSocket::connectWebSocketServer, 5L);
        }
    }

    public static void send(JSONObject data) {
        try {
            if (WebSocket.session != null) {
                if (WebSocket.session.isOpen()) {
                    JSONObject message = new JSONObject();
                    message.put("data", data);
                    message.put("data", data);
                    WebSocket.session.getAsyncRemote().sendText(message.toJSONString());
                } else {
                    WebSocket.session = null;
                }
            }
        } catch (Exception ignored) {
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        WebSocket.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        Bukkit.getPluginManager().callEvent(new WebSocketEvent(JSONObject.parseObject(message)));
    }

    @OnClose
    public void onClose() {
        WebSocket.session = null;
    }

    @OnError
    public void onError(Throwable e) {
        WebSocket.session = null;
        connectWebSocketServer();
        e.printStackTrace();
    }
}
