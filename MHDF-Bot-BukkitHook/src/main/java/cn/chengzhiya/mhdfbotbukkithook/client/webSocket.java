package cn.chengzhiya.mhdfbotbukkithook.client;

import cn.chengzhiya.mhdfbotapi.entity.PlayerData;
import cn.chengzhiya.mhdfbotbukkithook.main;
import cn.chengzhiya.mhdfbotbukkithook.util.Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.getPlayerDataList;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.*;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;

@ClientEndpoint
public final class webSocket {
    @Getter
    public static Session session;

    public static void send(String message) {
        if (session != null) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            } else {
                session = null;
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        webSocket.session = session;
        ColorLog("&e已连接至websocket服务端(" + main.main.getConfig().getString("BotWebSocketServerHost") + ")!");
    }

    @OnClose
    public void onClose() {
        session = null;
        ColorLog("&cwebocket服务端异常 正在尝试重新连接!");
        if (Bukkit.getPluginManager().getPlugin("MHDF-Bot-BukkitHook") != null) {
            connectWebsocketServer();
        }
    }

    @OnMessage
    public void onMessage(String messageString) {
        JSONObject message = JSON.parseObject(messageString);
        switch (message.getString("action")) {
            case "atQQ": {
                Bukkit.getScheduler().runTaskAsynchronously(main.main, () -> {
                    Long QQ = message.getJSONObject("params").getLong("QQ");
                    if (ifPlayerDataExist(QQ)) {
                        for (PlayerData playerData : getPlayerDataList(QQ)) {
                            if (Bukkit.getPlayer(playerData.getPlayerName()) != null) {
                                Player player = Bukkit.getPlayer(playerData.getPlayerName());

                                runAction(player, main.main.getConfig().getStringList("Actions.AtQQ"));
                            }
                        }
                    }
                });
                break;
            }
            case "bind": {
                Bukkit.getScheduler().runTaskAsynchronously(main.main, () -> {
                    String playerName = message.getJSONObject("params").getString("playerName");
                    if (Bukkit.getPlayer(playerName) != null) {
                        Player player = Bukkit.getPlayer(playerName);

                        runAction(player, main.main.getConfig().getStringList("Actions.BindDone"));
                    }
                });
                break;
            }
            case "getEnableVerify": {
                enableVerify = message.getJSONObject("params").getBoolean("enable");
                break;
            }
            case "getVerifyCode": {
                Util.getVerifyCodeHashMap().put(message.getJSONObject("params").getString("playerName"), message.getJSONObject("params").getIntValue("code"));
                break;
            }
            default:
        }
    }

    @OnError
    public void onError(Throwable e) {
        try {
            session.close();
        } catch (IOException ignored) {
        }

        session = null;

        throw new RuntimeException(e);
    }
}
