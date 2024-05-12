package cn.chengzhiya.mhdfbotbukkithook;

import cn.chengzhiya.mhdfbotapi.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbukkithook.client.webSocket;
import cn.chengzhiya.mhdfbotbukkithook.command.reload;
import cn.chengzhiya.mhdfbotbukkithook.hook.PlaceholderAPI;
import cn.chengzhiya.mhdfbotbukkithook.task.HeartBeat;
import cn.chengzhiya.mhdfbotbukkithook.task.SendMessage;
import cn.chengzhiya.mhdfbotbukkithook.task.UpdateData;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.WebSocketContainer;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.*;
import static cn.chengzhiya.mhdfbotbukkithook.client.webSocket.close;
import static cn.chengzhiya.mhdfpluginapi.Util.ColorLog;
import static cn.chengzhiya.mhdfpluginapi.YamlFileUtil.SaveResource;

public final class main extends JavaPlugin {
    public static main main;
    @Getter
    public static PluginDescriptionFile descriptionFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ColorLog("&f============&6梦回东方-Q群机器人-Bukkit连接器&f============");
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
                getConfig().getBoolean("LoginSystemDatabaseSettings.isMHDFLogin") ? new DatabaseConfig(
                        getConfig().getString("LoginSystemDatabaseSettings.Host"),
                        getConfig().getString("LoginSystemDatabaseSettings.Database"),
                        getConfig().getString("LoginSystemDatabaseSettings.Username"),
                        getConfig().getString("LoginSystemDatabaseSettings.Password")
                ) : null
        );

        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(new webSocket(), new URI(Objects.requireNonNull(getConfig().getString("BotWebSocketServerHost"))));
            ColorLog("&e已连接至websocket服务端(" + getConfig().getString("BotWebSocketServerHost") + ")!");
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Objects.requireNonNull(getCommand("mhdfbotreload")).setExecutor(new reload());

        new SendMessage().runTaskTimerAsynchronously(this, 0L, 20L);
        new UpdateData().runTaskTimerAsynchronously(this, 0L, 20L);
        new HeartBeat().runTaskTimerAsynchronously(this, 0L, 20L);

        new PlaceholderAPI().register();

        ColorLog("&e插件启动完成!");
        ColorLog("&f============&6梦回东方-Q群机器人-Bukkit连接器&f============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;

        try {
            close();

            statement.close();
            dataSource.close();

            loginSystemStatement.close();
            loginSystemDataSource.close();
        } catch (Exception ignored) {
        }

        new PlaceholderAPI().unregister();

        ColorLog("&f============&6梦回东方-Q群机器人-Bukkit连接器&f============");
        ColorLog("&e插件已卸载!");
        ColorLog("&f============&6梦回东方-Q群机器人-Bukkit连接器&f============");
    }
}
