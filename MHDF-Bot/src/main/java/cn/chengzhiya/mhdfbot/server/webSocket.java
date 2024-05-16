package cn.chengzhiya.mhdfbot.server;

import cn.chengzhiya.mhdfbot.MHDFBot;
import cn.chengzhiya.mhdfbot.util.Util;
import cn.chengzhiya.mhdfbotapi.entity.Bans;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static cn.chengzhiya.mhdfbot.util.Util.*;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.getPlayerData;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;

@Component
@Slf4j
@Service
@ServerEndpoint("/MHDFBot")
public final class webSocket {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    public static void send(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                sessions.remove(session);
                throw new RuntimeException(e);
            }
        }
    }

    public static void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            sessions.remove(session);
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, String messageString) {
        JSONObject message = JSON.parseObject(messageString);
//        if (!message.getString("action").equals("heartBeat")) {
//            System.out.println(message.toJSONString());
//        }
        switch (message.getString("action")) {
            case "getEnableVerify": {
                JSONObject data = new JSONObject();
                data.put("action", "getEnableVerify");

                JSONObject params = new JSONObject();
                params.put("enable", Util.getConfig().getBoolean("BindSettings.Verify"));

                data.put("params", params);

                send(data.toJSONString());
                break;
            }
            case "getVerifyCode": {
                String playerName = message.getJSONObject("params").getString("playerName");

                JSONObject data = new JSONObject();
                data.put("action", "getVerifyCode");

                JSONObject params = new JSONObject();
                params.put("playerName", playerName);
                params.put("code", getVerifyCode(playerName));

                data.put("params", params);

                send(data.toJSONString());
                break;
            }
            case "removeVerifyCode": {
                removeVerifyCode(message.getJSONObject("params").getString("playerName"));
                break;
            }
            case "bans": {
                Bans bans = message.getJSONObject("params").toJavaObject(Bans.class);
                System.out.println(bans);

                String Message;

                switch (bans.getType()) {
                    case "ban": {
                        Message = i18n("Messages.BanSystem.Ban");
                        break;
                    }
                    case "mute": {
                        Message = i18n("Messages.BanSystem.Mute");
                        break;
                    }
                    default:
                        return;
                }

                Message = Message
                        .replaceAll("\\{Player}", bans.getPlayerName())
                        .replaceAll("\\{BindQQ}", ifPlayerDataExist(bans.getPlayerName()) ? String.valueOf(Objects.requireNonNull(getPlayerData(bans.getPlayerName())).getQQ()) : "未绑定")
                        .replaceAll("\\{Reason}", bans.getReason())
                        .replaceAll("\\{StartDate}", new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒").format(bans.getStartTime()))
                        .replaceAll("\\{EndDate}", bans.getEndTime() != 0 ? new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒").format(bans.getEndTime()) : "永久封禁");

                sendMessageToAllowUseBotGroups(MHDFBot.bot, Message);
                break;
            }
            default:
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
        sessions.remove(session);
    }
}
