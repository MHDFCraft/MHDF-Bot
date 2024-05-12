package cn.chengzhiya.mhdfbotbukkithook.task;

import com.alibaba.fastjson2.JSONObject;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.chengzhiya.mhdfbotbukkithook.client.webSocket.send;

public final class HeartBeat extends BukkitRunnable {
    @Override
    public void run() {
        {
            JSONObject data = new JSONObject();
            data.put("action", "heartBeat");

            send(data.toJSONString());
        }
    }
}
