package cn.chengzhiya.mhdfbotbukkithook.util;

import cn.chengzhiya.mhdfbotbukkithook.client.webSocket;
import cn.chengzhiya.mhdfbotbukkithook.main;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static cn.chengzhiya.mhdfbotbukkithook.client.webSocket.send;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;

public final class Util {
    @Getter
    public static final HashMap<String, Integer> VerifyCodeHashMap = new HashMap<>();
    public static WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    public static boolean enableVerify = false;

    public static void getEnableVerify() {
        {
            JSONObject data = new JSONObject();
            data.put("action", "getEnableVerify");

            send(data.toJSONString());
        }
    }

    public static String PAPIChatColor(OfflinePlayer Player, String Message) {
        return ChatColor(PlaceholderAPI.setPlaceholders(Player, Message));
    }

    public static void runAction(Player player, List<String> ActionList) {
        for (String Actions : ActionList) {
            String[] Action = Actions.split("\\|");
            switch (Action[0]) {
                case "[player]":
                    Bukkit.getScheduler().runTask(main.main, () -> player.chat("/" + PlaceholderAPI.setPlaceholders(player, Action[1])));
                    continue;
                case "[console]":
                    Bukkit.getScheduler().runTask(main.main, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, Action[1])));
                    player.closeInventory();
                    continue;
                case "[playsound]":
                    try {
                        player.playSound(player, Sound.valueOf(Action[1]), Float.parseFloat(Action[2]), Float.parseFloat(Action[3]));
                    } catch (Exception e) {
                        ColorLog("&c[MHDF-PluginAPI]不存在" + Action[1] + "这个音频");
                    }
                    continue;
                case "[playsound_pack]":
                    try {
                        player.playSound(player, Action[1], Float.parseFloat(Action[2]), Float.parseFloat(Action[3]));
                    } catch (Exception e) {
                        ColorLog("&c[MHDF-PluginAPI]不存在" + Action[1] + "这个音频");
                    }
                    continue;
                case "[message]":
                    player.sendMessage(PAPIChatColor(player, Action[1]).replaceAll(Action[0] + "\\|", "").replaceAll("\\|", "\n"));
                    continue;
                case "[title]":
                    player.sendTitle(PAPIChatColor(player, Action[1]), PAPIChatColor(player, Action[2]), Integer.parseInt(Action[3]), Integer.parseInt(Action[4]), Integer.parseInt(Action[5]));
                    continue;
                case "[actionbar]":
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PAPIChatColor(player, Action[1])));
                    continue;
            }
            ColorLog("&c[MHDF-PluginAPI]不存在" + Action[0] + "这个操作");
        }
    }

    public static void connectWebsocketServer() {
        try {
            container.connectToServer(new webSocket(), new URI(Objects.requireNonNull(main.main.getConfig().getString("BotWebSocketServerHost"))));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            ColorLog("&c无法正常连接至websocket服务端,无法加载绑定以及AT消息和更新验证码数据");
            if (Bukkit.getPluginManager().getPlugin("MHDF-Bot-BukkitHook") != null) {
                Bukkit.getScheduler().runTaskAsynchronously(main.main, Util::connectWebsocketServer);
            }
        }
    }
}
