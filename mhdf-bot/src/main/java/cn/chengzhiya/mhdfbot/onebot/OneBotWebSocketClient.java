package cn.chengzhiya.mhdfbot.onebot;

import cn.chengzhiya.mhdfbot.Main;
import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.enums.notice.NoticeType;
import cn.chengzhiya.mhdfbot.api.enums.notice.NotifySubType;
import cn.chengzhiya.mhdfbot.api.event.bot.HeartbeatEvent;
import cn.chengzhiya.mhdfbot.api.event.bot.LifecycleEvent;
import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.event.message.PrivateMessageEvent;
import cn.chengzhiya.mhdfbot.api.event.notice.*;
import cn.chengzhiya.mhdfbot.api.event.request.FriendRequestEvent;
import cn.chengzhiya.mhdfbot.api.event.request.GroupRequestEvent;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused"})
public final class OneBotWebSocketClient extends Endpoint {
    private final String websocketHost = Main.getConfigManager().getConfig().getString("oneBotSettings.websocketHost");
    private final String accessToken = Main.getConfigManager().getConfig().getString("oneBotSettings.accessToken");
    private final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    public Session session;

    /**
     * 连接服务器
     */
    public void connectServer() {
        try {
            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
                    .configurator(new ClientEndpointConfig.Configurator() {
                        @Override
                        public void beforeRequest(Map<String, List<String>> headers) {
                            if (accessToken != null) {
                                headers.put("Authorization", Collections.singletonList("Bearer " + accessToken));
                            }
                        }
                    })
                    .build();

            container.connectToServer(this, clientEndpointConfig, new URI(websocketHost));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            MHDFBot.getLogger().info("无法正常连接至websocket服务端,正在重试!");
            MHDFBot.getScheduler().runTaskLater(this::connectServer, 5L);
        }
    }

    /**
     * 向服务器发送消息
     *
     * @param message 消息
     */
    public void send(String message) {
        try {
            if (this.session != null && this.session.isOpen()) {
                this.session.getAsyncRemote().sendText(message);
            } else {
                this.session = null;
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;

        session.addMessageHandler(String.class, this::handlerMessage);
        MHDFBot.getLogger().info("websocket服务端连接成功!");
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        this.session = null;
        MHDFBot.getLogger().info("websocket服务端已离线!");
        this.connectServer();
    }

    @OnError
    public void onError(Session session, Throwable e) {
        this.session = null;
        this.connectServer();
        MHDFBot.getLogger().error(e);
    }

    public void handlerMessage(String message) {
        JSONObject data = JSONObject.parseObject(message);
        if (data.getString("post_type") != null) {
            switch (data.getString("post_type")) {
                case "message" -> {
                    switch (data.getString("message_type")) {
                        case "group" -> MHDFBot.getListenerManager().callEvent(new GroupMessageEvent(data));
                        case "private" -> MHDFBot.getListenerManager().callEvent(new PrivateMessageEvent(data));
                        default -> MHDFBot.getLogger().info(message);
                    }
                }
                case "meta_event" -> {
                    switch (data.getString("meta_event_type")) {
                        case "lifecycle" -> MHDFBot.getListenerManager().callEvent(new LifecycleEvent(data));
                        case "heartbeat" -> MHDFBot.getListenerManager().callEvent(new HeartbeatEvent(data));
                        default -> MHDFBot.getLogger().info(message);
                    }
                }
                case "notice" -> {
                    switch (NoticeType.get(data.getString("notice_type"))) {
                        case GROUP_UPLOAD -> MHDFBot.getListenerManager().callEvent(new GroupUploadEvent(data));
                        case GROUP_ADMIN -> MHDFBot.getListenerManager().callEvent(new GroupAdminEvent(data));
                        case GROUP_BAN -> MHDFBot.getListenerManager().callEvent(new GroupBanEvent(data));
                        case GROUP_DECREASE -> MHDFBot.getListenerManager().callEvent(new GroupDecreaseEvent(data));
                        case GROUP_INCREASE -> MHDFBot.getListenerManager().callEvent(new GroupIncreaseEvent(data));
                        case GROUP_RECALL -> MHDFBot.getListenerManager().callEvent(new GroupRecallEvent(data));
                        case GROUP_CARD -> MHDFBot.getListenerManager().callEvent(new GroupCardEvent(data));
                        case FRIEND_RECALL -> MHDFBot.getListenerManager().callEvent(new FriendRecallEvent(data));
                        case NOTIFY -> {
                            switch (NotifySubType.get(data.getString("sub_type"))) {
                                case POKE -> MHDFBot.getListenerManager().callEvent(new PokeEvent(data));
                                case LUCKY_KING -> MHDFBot.getListenerManager().callEvent(new LuckyKingEvent(data));
                                case HONOR -> MHDFBot.getListenerManager().callEvent(new HonorEvent(data));
                                case INPUT_STATUS -> MHDFBot.getListenerManager().callEvent(new InputStatusEvent(data));
                                default -> MHDFBot.getLogger().info(message);
                            }
                        }
                        default -> MHDFBot.getLogger().info(message);
                    }
                }
                case "request" -> {
                    switch (data.getString("request_type")) {
                        case "friend" -> MHDFBot.getListenerManager().callEvent(new FriendRequestEvent(data));
                        case "group" -> MHDFBot.getListenerManager().callEvent(new GroupRequestEvent(data));
                        default -> MHDFBot.getLogger().info(message);
                    }
                }
                default -> MHDFBot.getLogger().info(message);
            }
        }
    }
}
