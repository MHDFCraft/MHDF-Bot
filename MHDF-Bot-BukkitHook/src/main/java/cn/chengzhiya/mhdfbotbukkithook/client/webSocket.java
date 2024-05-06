package cn.chengzhiya.mhdfbotbukkithook.client;

import cn.chengzhiya.mhdfbotbukkithook.main;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Objects;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.getPlayerData;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.runAction;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

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
                        if (getPlayerData(QQ) != null) {
                            if (Bukkit.getPlayer(Objects.requireNonNull(getPlayerData(QQ)).getPlayerName()) != null) {
                                Player player = Bukkit.getPlayer(Objects.requireNonNull(getPlayerData(QQ)).getPlayerName());

                                String[] Title = Objects.requireNonNull(main.main.getConfig().getString("Messages.AtQQ")).split("\\|");
                                String[] Sound = Objects.requireNonNull(main.main.getConfig().getString("Sound.AtQQ")).split("\\|");

                                Bukkit.getScheduler().runTask(main.main, () -> {
                                    player.sendTitle(ChatColor(Title[0]), ChatColor(Title[1]), Integer.parseInt(Title[2]), Integer.parseInt(Title[3]), Integer.parseInt(Title[4]));
                                    player.playSound(player, org.bukkit.Sound.valueOf(Sound[0]), Float.parseFloat(Sound[1]), Float.parseFloat(Sound[2]));
                                });
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
