package cn.chengzhiya.mhdfbotbukkithook;

import cn.chengzhiya.mhdfbotbukkithook.minecraft.WebSocketClient;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main instance;
    @Getter
    private static WebSocketClient webSocketClient;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        webSocketClient = new WebSocketClient();
        getWebSocketClient().connectServer();

        getLogger().info("梦之机器人框架服务端Hook已启动!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;

        getLogger().info("梦之机器人框架服务端Hook已卸载!");
    }
}
