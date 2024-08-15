package cn.ChengZhiYa.MHDFBot.util;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Event;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ListenerUtil {
    private static final List<Listener> listenerList = new ArrayList<>();

    public static void registerListener(Listener listener) {
        listenerList.add(listener);
    }

    public static void callEvent(Event event) {
        for (Listener listener : listenerList) {
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
