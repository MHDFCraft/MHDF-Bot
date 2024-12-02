package cn.chengzhiya.mhdfbot.api.plugin;

@SuppressWarnings("EmptyMethod")
public interface Plugin {
    /**
     * 插件启动时
     */
    void onEnable();

    /**
     * 插件关闭时
     */
    void onDisable();
}
