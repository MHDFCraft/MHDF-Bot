package cn.chengzhiya.mhdfbot.api.plugin;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.entity.plugin.PluginInfo;
import lombok.Data;
import org.apache.logging.log4j.Logger;

@Data
@SuppressWarnings("unused")
public abstract class JavaPlugin implements Plugin {
    private PluginInfo pluginInfo;

    public Logger getLogger() {
        return MHDFBot.getLogger(pluginInfo.getName());
    }
}
