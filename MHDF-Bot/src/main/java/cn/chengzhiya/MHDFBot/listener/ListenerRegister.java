package cn.ChengZhiYa.MHDFBot.listener;

import cn.ChengZhiYa.MHDFBot.MHDFBot;
import cn.ChengZhiYa.MHDFBot.listener.main.Lifecycle;
import cn.ChengZhiYa.MHDFBot.listener.main.PrivateMessage;
import cn.ChengZhiYa.MHDFBot.listener.main.WebSocket;
import cn.ChengZhiYa.MHDFBot.util.ListenerUtil;

public final class ListenerRegister {
    public static void registerListeners() {
        ListenerUtil.registerListener(MHDFBot.getBotInfo(),new Lifecycle());
//        ListenerUtil.registerListener(new GroupMessage());
        ListenerUtil.registerListener(MHDFBot.getBotInfo(),new PrivateMessage());
        ListenerUtil.registerListener(MHDFBot.getBotInfo(),new WebSocket());
    }
}
