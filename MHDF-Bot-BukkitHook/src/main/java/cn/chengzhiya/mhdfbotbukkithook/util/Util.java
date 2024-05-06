package cn.chengzhiya.mhdfbotbukkithook.util;

import cn.chengzhiya.mhdfbotbukkithook.main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;

public class Util {
    public static String PAPIChatColor(OfflinePlayer Player, String Message) {
        return ChatColor(PlaceholderAPI.setPlaceholders(Player, Message));
    }

    public static void runAction(Player player, List<String> ActionList) {
        for (String Actions : ActionList) {
            String[] Action = Actions.split("\\|");
            if (Action[0].equals("[player]")) {
                Bukkit.getScheduler().runTask(main.main, () -> player.chat("/" + PlaceholderAPI.setPlaceholders(player, Action[1])));
                continue;
            }
            if (Action[0].equals("[console]")) {
                Bukkit.getScheduler().runTask(main.main, () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, Action[1])));
                player.closeInventory();
                continue;
            }
            if (Action[0].equals("[playsound]")) {
                try {
                    player.playSound(player, Sound.valueOf(Action[1]), Float.parseFloat(Action[2]), Float.parseFloat(Action[3]));
                } catch (Exception e) {
                    ColorLog("&c[MHDF-PluginAPI]不存在" + Action[1] + "这个音频");
                }
                continue;
            }
            if (Action[0].equals("[playsound_pack]")) {
                try {
                    player.playSound(player, Action[1], Float.parseFloat(Action[2]), Float.parseFloat(Action[3]));
                } catch (Exception e) {
                    ColorLog("&c[MHDF-PluginAPI]不存在" + Action[1] + "这个音频");
                }
                continue;
            }
            if (Action[0].equals("[message]")) {
                player.sendMessage(PAPIChatColor(player, Action[1]).replaceAll(Action[0] + "\\|", "").replaceAll("\\|", "\n"));
                continue;
            }
            if (Action[0].equals("[title]")) {
                player.sendTitle(PAPIChatColor(player, Action[1]), PAPIChatColor(player, Action[2]), Integer.parseInt(Action[3]), Integer.parseInt(Action[4]), Integer.parseInt(Action[5]));
                continue;
            }
            if (Action[0].equals("[actionbar]")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PAPIChatColor(player, Action[1])));
                continue;
            }
            ColorLog("&c[MHDF-PluginAPI]不存在" + Action[0] + "这个操作");
        }
    }
}
