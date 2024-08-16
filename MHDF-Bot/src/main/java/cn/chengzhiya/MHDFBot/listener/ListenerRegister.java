package cn.ChengZhiYa.MHDFBot.listener;

import cn.ChengZhiYa.MHDFBot.listener.main.Lifecycle;
import cn.ChengZhiYa.MHDFBot.listener.main.PrivateMessage;
import cn.ChengZhiYa.MHDFBot.listener.main.WebSocket;
import cn.ChengZhiYa.MHDFBot.util.ListenerUtil;

public final class ListenerRegister {
    public static void registerListeners() {
        ListenerUtil.registerListener(new Lifecycle());
//        ListenerUtil.registerListener(new GroupMessage());
        ListenerUtil.registerListener(new PrivateMessage());
        ListenerUtil.registerListener(new WebSocket());
    }
}
