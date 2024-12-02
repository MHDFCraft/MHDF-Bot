package cn.chengzhiya.mhdfbot.minecraft;

import cn.chengzhiya.mhdfbot.Main;
import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.event.minecraft.MinecraftWebsocketMessageEvent;
import cn.chengzhiya.mhdfbot.api.runnable.MHDFBotRunnable;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.websocket.server.WsSci;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/ws")
@SuppressWarnings("unused")
public final class MinecraftWebSocketServer {
    private final int port = Main.getConfigManager().getConfig().getInt("webSocketServerSettings.port");
    private final Set<Session> sessions = new CopyOnWriteArraySet<>();

    /**
     * 启动服务器
     */
    public void startServer() {
        try {
            Logger tomcatLogger = Logger.getLogger("org.apache");
            tomcatLogger.setLevel(Level.OFF);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.OFF);
            tomcatLogger.addHandler(consoleHandler);

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(port);
            tomcat.getConnector();

            Context ctx = tomcat.addWebapp("", new File(".").getAbsolutePath());
            ctx.addServletContainerInitializer(new WsSci(), null);

            tomcat.start();
            MHDFBot.getLogger().info("websocket服务端启动成功(0.0.0.0:{})!", port);
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向所有连接客户端发送消息
     *
     * @param action 操作
     * @param data   数据
     */
    public void send(String action, JSONObject data) {
        for (Session session : this.sessions) {
            send(session, action, data);
        }
    }

    /**
     * 向指定客户端发送消息
     *
     * @param session 客户端实例
     * @param action  操作
     * @param data    数据
     */
    public void send(Session session, String action, JSONObject data) {
        try {
            JSONObject sendData = new JSONObject();
            sendData.put("action", action);
            sendData.put("data", data);

            session.getBasicRemote().sendText(sendData.toJSONString());
        } catch (IOException e) {
            this.sessions.remove(session);
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.sessions.add(session);
        MHDFBot.getLogger().info("客户端{}连接至websocket服务端!", session.getId());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        JSONObject data = JSONObject.parseObject(message);
        MHDFBot.getListenerManager().callEvent(new MinecraftWebsocketMessageEvent(data));
    }

    @OnClose
    @OnError
    public void onClose(Session session) {
        this.sessions.remove(session);
        MHDFBot.getLogger().info("客户端{}断开websocket服务端!", session.getId());
    }

    public static class HeartBeat extends MHDFBotRunnable {
        @Override
        public void run() {
            MHDFBot.getMinecraftWebSocketServer().send("heartBeat", new JSONObject());
        }
    }
}
