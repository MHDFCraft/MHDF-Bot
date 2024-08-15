package cn.ChengZhiYa.mhdfbotbukkithook.task;

import cn.ChengZhiYa.mhdfbotbukkithook.client.webSocket;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public final class WebSocketQueue extends BukkitRunnable {
    public static List<String> messageList = new ArrayList<>();

    public void addMessage(String message) {
        messageList.add(message);
    }

    @Override
    public void run() {
        if (!messageList.isEmpty()) {
            webSocket.getSession().getAsyncRemote().sendText(messageList.get(0));
            messageList.remove(0);
        }
    }
}
