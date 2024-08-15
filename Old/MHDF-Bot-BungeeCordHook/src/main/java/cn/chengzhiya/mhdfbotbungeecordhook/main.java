package cn.ChengZhiYa.mhdfbotbungeecordhook;

import cn.ChengZhiYa.mhdfbotapi.entity.DatabaseConfig;
import cn.ChengZhiYa.mhdfbotbungeecordhook.listener.Chat;
import cn.ChengZhiYa.mhdfbotbungeecordhook.listener.ServerConnect;
import cn.ChengZhiYa.mhdfbotbungeecordhook.util.Util;
import net.md_5.bungee.api.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.ChengZhiYa.mhdfbotapi.util.DatabaseUtil.intiDatabase;
import static cn.ChengZhiYa.mhdfbotbungeecordhook.util.Util.reloadConfig;
import static cn.ChengZhiYa.mhdfbotbungeecordhook.util.Util.saveConfig;

public final class main extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(main.class);
    public static main main;

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;

        saveConfig();
        reloadConfig();

        intiDatabase(
                new DatabaseConfig(
                        Util.getConfig().getString("DatabaseSettings.Host"),
                        Util.getConfig().getString("DatabaseSettings.Database"),
                        Util.getConfig().getString("DatabaseSettings.Username"),
                        Util.getConfig().getString("DatabaseSettings.Password")
                ),
                Util.getConfig().getBoolean("LoginSystemDatabaseSettings.isMHDFLogin") ? new DatabaseConfig(
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Host"),
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Database"),
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Username"),
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Password")
                ) : null
        );

        getProxy().getPluginManager().registerListener(this, new Chat());
        getProxy().getPluginManager().registerListener(this, new ServerConnect());

        log.info("============梦回东方-Q群机器人-BC连接器============");
        log.info("插件启动完成!");
        log.info("============梦回东方-Q群机器人-BC连接器============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        main = null;

        log.info("=======梦回东方-Q群机器人-BC连接器=======");
        log.info("插件已卸载!");
        log.info("=======梦回东方-Q群机器人-BC连接器=======");
    }
}
