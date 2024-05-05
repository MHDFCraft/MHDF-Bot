package cn.chengzhiya.mhdfbotbungeecordhook;

import cn.chengzhiya.mhdfbotapi.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbungeecordhook.listener.Chat;
import cn.chengzhiya.mhdfbotbungeecordhook.listener.ServerConnect;
import cn.chengzhiya.mhdfbotbungeecordhook.util.Util;
import net.md_5.bungee.api.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.intiDatabase;
import static cn.chengzhiya.mhdfbotbungeecordhook.util.Util.reloadConfig;
import static cn.chengzhiya.mhdfbotbungeecordhook.util.Util.saveConfig;

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
                new DatabaseConfig(
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Host"),
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Database"),
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Username"),
                        Util.getConfig().getString("LoginSystemDatabaseSettings.Password")
                )
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
        log.info("&e插件已卸载!");
        log.info("=======梦回东方-Q群机器人-BC连接器=======");
    }
}
