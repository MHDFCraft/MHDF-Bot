package cn.chengzhiya.mhdfbotbukkithook;

import cn.chengzhiya.mhdfbotapi.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbukkithook.command.reload;
import cn.chengzhiya.mhdfbotbukkithook.task.SendMessage;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.intiDatabase;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;
import static cn.chengzhiya.mhdfpluginapi.YamlFileUtil.SaveResource;

public final class main extends JavaPlugin {
    public static main main;
    @Getter
    public static PluginDescriptionFile descriptionFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;
        descriptionFile = getDescription();

        {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            SaveResource(getDataFolder().getPath(), "config.yml", "config.yml", false);
        }

        reloadConfig();

        intiDatabase(
                new DatabaseConfig(
                        getConfig().getString("DatabaseSettings.Host"),
                        getConfig().getString("DatabaseSettings.Database"),
                        getConfig().getString("DatabaseSettings.Username"),
                        getConfig().getString("DatabaseSettings.Password")
                ),
                new DatabaseConfig(
                        getConfig().getString("LoginSystemDatabaseSettings.Host"),
                        getConfig().getString("LoginSystemDatabaseSettings.Database"),
                        getConfig().getString("LoginSystemDatabaseSettings.Username"),
                        getConfig().getString("LoginSystemDatabaseSettings.Password")
                )
        );

        Objects.requireNonNull(getCommand("mhdfbotreload")).setExecutor(new reload());
        new SendMessage().runTaskTimerAsynchronously(this, 0L, 20L);

        ColorLog("============梦回东方-Q群机器人-BC连接器============");
        ColorLog("插件启动完成!");
        ColorLog("============梦回东方-Q群机器人-BC连接器============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;

        ColorLog("&f============&6梦回东方-Q群机器人-BC连接器&f============");
        ColorLog("&e插件已卸载!");
        ColorLog("&f============&6梦回东方-Q群机器人-BC连接器&f============");
    }
}
