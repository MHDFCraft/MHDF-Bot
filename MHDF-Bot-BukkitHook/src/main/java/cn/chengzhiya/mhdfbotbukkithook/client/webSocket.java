package cn.chengzhiya.mhdfbotbukkithook.client;

import cn.chengzhiya.mhdfbotapi.entity.PlayerData;
import cn.chengzhiya.mhdfbotbukkithook.main;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.getPlayerDataList;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.runAction;

@ClientEndpoint
public final class webSocket {
    public static Session session;

    public static void send(String message) {
        session.getAsyncRemote().sendText(message);
    }

    public static void close() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        webSocket.session = session;
    }

    @OnMessage
    public void onMessage(String messageString) {
        JSONObject message = JSON.parseObject(messageString);
        switch (message.getString("action")) {
            case "atQQ":
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
            case "bind":
                Bukkit.getScheduler().runTaskAsynchronously(main.main, () -> {
                    String playerName = message.getJSONObject("params").getString("playerName");
                    if (Bukkit.getPlayer(playerName) != null) {
                        Player player = Bukkit.getPlayer(playerName);

                        runAction(player, main.main.getConfig().getStringList("Actions.BindDone"));
                    }
                });
                break;
            default:
        }
    }

    @OnError
    public void onError(Throwable e) throws IOException {
        session.close();
        throw new RuntimeException(e);
    }
}
