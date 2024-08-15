package cn.ChengZhiYa.mhdfbotbungeecordhook.util;

import cn.ChengZhiYa.mhdfbotbungeecordhook.main;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class Util {
    @Getter
    public static Configuration config;

    public static void saveConfig() {
        if (!main.main.getDataFolder().exists()) {
            main.main.getDataFolder().mkdir();
        }

        File ConfigFile = new File(main.main.getDataFolder(), "config.yml");

        if (!ConfigFile.exists()) {
            try (InputStream in = main.main.getResourceAsStream("config.yml")) {
                Files.copy(in, ConfigFile.toPath());
            } catch (IOException ignored) {
            }
        }
    }

    public static void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(main.main.getDataFolder(), "config.yml"));
        } catch (IOException ignored) {
        }
    }
}
