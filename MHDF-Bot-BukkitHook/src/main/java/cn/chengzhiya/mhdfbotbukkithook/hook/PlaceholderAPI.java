package cn.chengzhiya.mhdfbotbukkithook.hook;

import cn.chengzhiya.mhdfbotbukkithook.main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.getPlayerData;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfpluginapi.Util.ChatColor;

public final class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI() {
    }

    @Override
    public @NotNull String getAuthor() {
        return "chengzhiya";
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
            return ifPlayerDataExist(player.getName()) ? ChatColor(main.main.getConfig().getString("Bind")) : ChatColor(main.main.getConfig().getString("NotBind"));
        }
        if (params.equalsIgnoreCase("getqq")) {
            return String.valueOf(Objects.requireNonNull(getPlayerData(player.getName())).getQQ());
        }
        if (params.equalsIgnoreCase("getchattimes")) {
            return String.valueOf(Objects.requireNonNull(getPlayerData(player.getName())).getChatTimes());
        }
        if (params.equalsIgnoreCase("getdaychattimes")) {
            return String.valueOf(Objects.requireNonNull(getPlayerData(player.getName())).getDayChatTimes());
        }
        return null;
    }
}
