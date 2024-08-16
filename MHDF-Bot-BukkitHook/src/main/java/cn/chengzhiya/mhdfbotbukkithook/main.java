package cn.ChengZhiYa.MHDFBotBukkitHook;

import cn.ChengZhiYa.MHDFBotBukkitHook.listener.WebSocket;
import cn.ChengZhiYa.MHDFBotBukkitHook.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFBotBukkitHook.client.WebSocket.connectWebSocketServer;
import static cn.ChengZhiYa.MHDFBotBukkitHook.util.FileUtil.createDir;
import static cn.ChengZhiYa.MHDFBotBukkitHook.util.MessageUtil.colorLog;

public final class main extends JavaPlugin {
    public static main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        createDir(getDataFolder());
        ConfigUtil.saveResource("./", "config.yml", "config.yml", false);

        reloadConfig();

        connectWebSocketServer();

        Bukkit.getPluginManager().registerEvents(new WebSocket(), this);

        colorLog("&f============&6梦之机器人-子服连接器&f============");
        colorLog("&e插件启动完成!");
        colorLog("&f============&6梦之机器人-子服连接器&f============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;

        colorLog("&f============&6梦之机器人-子服连接器&f============");
        colorLog("&e插件已卸载!");
        colorLog("&f============&6梦之机器人-子服连接器&f============");
    }
}
