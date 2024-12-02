package cn.chengzhiya.mhdfbotbukkithook.minecraft;

import cn.chengzhiya.mhdfbotbukkithook.Main;
import cn.chengzhiya.mhdfbotbukkithook.event.WebSocketEvent;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public final class WebSocketClient {
    private final String websocketHost = Main.instance.getConfig().getString("webSocketSettings.host");
    private final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    public Session session;

    /**
     * 连接服务器
     */
    public void connectServer() {
        try {
            this.container.connectToServer(this, new URI(Objects.requireNonNull(websocketHost)));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            Main.instance.getLogger().info("无法正常连接至websocket服务端!");
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.instance, this::connectServer, 5L);
        }
    }

    /**
     * 向服务器发送消息
     *
     * @param action 操作
     * @param data   数据
     */
    public void send(String action, JSONObject data) {
        try {
            if (this.session != null && this.session.isOpen()) {
                JSONObject message = new JSONObject();
                message.put("server_type", "bukkit");
                message.put("action", action);
                message.put("data", data);

                this.session.getAsyncRemote().sendText(message.toJSONString());
            }
            this.session = null;
        } catch (Exception ignored) {
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        Bukkit.getPluginManager().callEvent(new WebSocketEvent(JSONObject.parseObject(message)));
    }

    @OnClose
    public void onClose() {
        this.session = null;
        this.connectServer();
    }

    @OnError
    public void onError(Throwable e) {
        this.session = null;
        this.connectServer();
        e.printStackTrace();
    }
}
