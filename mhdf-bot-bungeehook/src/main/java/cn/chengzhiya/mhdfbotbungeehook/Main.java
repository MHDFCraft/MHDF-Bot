package cn.chengzhiya.mhdfbotbungeehook;

import cn.chengzhiya.mhdfbotbungeehook.minecraft.WebSocketClient;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public final class Main extends Plugin {
    @Getter
    private static WebSocketClient webSocketClient;
    public static Main instance;
    @Getter
    private Configuration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.saveDefaultConfig();
        this.reloadConfig();

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

    /**
     * 保存默认配置文件
     */
    private void saveDefaultConfig() {
        saveResource("config.yml", "config.yml", false);
    }

    /**
     * 重载配置文件
     */
    private void reloadConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存资源
     *
     * @param filePath     保存目录
     * @param resourcePath 资源目录
     * @param replace      替换文件
     */
    private void saveResource(String filePath, String resourcePath, boolean replace) {
        File file = new File(filePath);
        if (file.exists() && !replace) {
            return;
        }

        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            throw new RuntimeException("找不到资源: " + resourcePath);
        }

        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.setUseCaches(false);

        try (InputStream in = url.openStream()) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                if (in == null) {
                    throw new RuntimeException("读取资源 " + resourcePath + " 的时候发生了错误");
                }

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法保存资源", e);
        }
    }
}
