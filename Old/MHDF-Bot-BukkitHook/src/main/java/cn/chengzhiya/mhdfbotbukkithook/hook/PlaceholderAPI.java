package cn.ChengZhiYa.mhdfbotbukkithook.hook;

import cn.ChengZhiYa.mhdfbotbukkithook.main;
import cn.ChengZhiYa.mhdfbotbukkithook.util.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.mhdfbotapi.util.DatabaseUtil.getPlayerData;
import static cn.ChengZhiYa.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.ChengZhiYa.mhdfpluginapi.Util.ChatColor;

public final class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI() {
    }

    public static void registerPlaceholders() {
        new PlaceholderAPI().register();
    }

    public static void unregisterPlaceholders() {
        new PlaceholderAPI().unregister();
    }

    @Override
    public @NotNull String getAuthor() {
        return "ChengZhiYa";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "MHDFBot";
    }

    @Override
    public @NotNull String getVersion() {
        return main.getDescriptionFile().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("isbind")) {
            return ifPlayerDataExist(player.getName()) ? ChatColor(main.main.getConfig().getString("Messages.Bind")) : ChatColor(main.main.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getqq")) {
            return ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(getPlayerData(player.getName())).getQQ()) : ChatColor(main.main.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getchattimes")) {
            return ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(getPlayerData(player.getName())).getChatTimes()) : ChatColor(main.main.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getdaychattimes")) {
            return ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(getPlayerData(player.getName())).getDayChatTimes()) : ChatColor(main.main.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getVerifyCode")) {
            return Util.getVerifyCodeHashMap().get(player.getName()) != null ? String.valueOf(Util.getVerifyCodeHashMap().get(player.getName())) : "";
        }
        return null;
    }
}
