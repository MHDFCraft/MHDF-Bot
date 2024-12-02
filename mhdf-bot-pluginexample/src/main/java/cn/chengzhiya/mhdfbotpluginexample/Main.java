package cn.chengzhiya.mhdfbotpluginexample;

import cn.chengzhiya.mhdfbot.api.plugin.JavaPlugin;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("hello World!");
    }

    @Override
    public void onDisable() {
        getLogger().info("goodbye World!");
    }
}
