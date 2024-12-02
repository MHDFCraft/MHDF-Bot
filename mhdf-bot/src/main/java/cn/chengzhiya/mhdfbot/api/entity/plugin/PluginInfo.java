package cn.chengzhiya.mhdfbot.api.entity.plugin;

import cn.chengzhiya.mhdfbot.api.enums.plugin.PluginStatus;
import cn.chengzhiya.mhdfbot.api.plugin.JavaPlugin;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.jar.JarFile;

@Getter
public final class PluginInfo {
    private final String name;
    private final String version;
    private final String main;
    private final List<String> authoendrs;
    @Setter
    private JarFile jarFile;
    @Setter
    private JavaPlugin plugin;
    @Setter
    private PluginStatus pluginStatus;

    public PluginInfo(String name, String version, String main, List<String> authoendrs) {
        this.name = name;
        this.version = version;
        this.main = main;
        this.authoendrs = authoendrs;
    }
}
