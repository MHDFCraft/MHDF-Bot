package cn.chengzhiya.mhdfbot;

import cn.chengzhiya.mhdfbot.entity.YamlConfiguration;
import cn.chengzhiya.mhdfbotapi.entity.DatabaseConfig;
import com.mikuac.shiro.core.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.time.LocalDate;

import static cn.chengzhiya.mhdfbot.util.Util.*;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.*;

@SpringBootApplication
@EnableScheduling
public class MHDFBot {
    public static Bot bot;

    private static void saveDefaultConfigFiles() {
        saveResource("./", "application.yml", "application.yml", false);
        saveResource("./", "config.yml", "config.yml", false);
        saveResource("./", "lang.yml", "lang.yml", false);
        createFile("./", "cache.yml", false);

        reloadConfig();
        reloadLang();
    }

    public static void main(String[] args) {
        saveDefaultConfigFiles();

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

        {
            File cacheFile = new File("./", "cache.yml");
            YamlConfiguration cache = YamlConfiguration.loadConfiguration(cacheFile);
            Integer Day = cache.getInt("Day");
            if (Day != null) {
                if (Day != LocalDate.now().getDayOfMonth()) {
                    clearMarry();
                    clearDayChatTimes();
                }
            }
            cache.set("Day", LocalDate.now().getDayOfMonth());
            cache.save(cacheFile);
        }

        SpringApplication.run(MHDFBot.class, args);
    }
}
