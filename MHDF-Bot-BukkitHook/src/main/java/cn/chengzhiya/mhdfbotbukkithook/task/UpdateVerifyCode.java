package cn.chengzhiya.mhdfbotbukkithook.task;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.ifPlayerDataExist;
import static cn.chengzhiya.mhdfbotbukkithook.client.webSocket.send;
import static cn.chengzhiya.mhdfbotbukkithook.util.Util.enableVerify;

public final class UpdateVerifyCode extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!ifPlayerDataExist(player.getName())) {
                if (enableVerify) {
                    JSONObject data = new JSONObject();
                    data.put("action", "getVerifyCode");

                    JSONObject params = new JSONObject();
                    params.put("playerName", player.getName());

                    data.put("params", params);

                    send(data.toJSONString());
                }
            }
        }
    }
}
