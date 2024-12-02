package cn.chengzhiya.mhdfbot.manager;

import cn.chengzhiya.mhdfbot.api.entity.config.YamlConfiguration;
import cn.chengzhiya.mhdfbot.api.util.FileUtil;
import lombok.Getter;

import java.io.File;

@Getter
public final class ConfigManager {
    private YamlConfiguration config;

    /**
     * 保存默认配置文件
     */
    public void saveDefaultConfig() {
        FileUtil.saveResource("config.yml", "config.yml", false);
    }

    /**
     * 重载配置文件
     */
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(new File("config.yml"));
    }
}
