package cn.chengzhiya.mhdfbot.api.manager;

import cn.chengzhiya.mhdfbot.api.entity.plugin.PluginInfo;
import cn.chengzhiya.mhdfbot.api.event.Event;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;

@Getter
@SuppressWarnings("unused")
public final class ListenerManager {
    private final HashMap<Listener, PluginInfo> listenerHashMap = new HashMap<>();

    /**
     * 注册指定监听器实例
     *
     * @param pluginInfo 插件信息实例
     * @param listener   监听器实例
     */
    public void registerListener(PluginInfo pluginInfo, Listener listener) {
        getListenerHashMap().put(listener, pluginInfo);
    }

    /**
     * 响应指定事件实例
     *
     * @param event 事件实例
     */
    public void callEvent(Event event) {
        for (Listener listener : getListenerHashMap().keySet()) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    try {
                        method.invoke(listener, event);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }
}
