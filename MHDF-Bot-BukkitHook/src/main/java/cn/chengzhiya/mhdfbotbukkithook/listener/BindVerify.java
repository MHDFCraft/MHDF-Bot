package cn.chengzhiya.mhdfbotbukkithook.listener;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.client.webSocket.send;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.enableVerify;

public final class BindVerify implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!ifPlayerDataExist(event.getPlayer().getName())) {
            if (enableVerify) {
                JSONObject data = new JSONObject();
                data.put("action", "removeVerifyCode");

                JSONObject params = new JSONObject();
                params.put("playerName", event.getPlayer().getName());

                data.put("params", params);

                send(data.toJSONString());
            }
        }
    }
}
