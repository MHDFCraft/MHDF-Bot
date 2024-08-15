package cn.ChengZhiYa.mhdfbotbukkithook.hook;

import cn.ChengZhiYa.mhdfbotapi.entity.Bans;
import cn.ChengZhiYa.mhdfbotbukkithook.main;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

import static cn.ChengZhiYa.mhdfbotbukkithook.client.webSocket.send;

public final class LiteBans {
    public static Events.Listener listener = new Events.Listener() {
        @Override
        public void entryAdded(Entry entry) {
            Bukkit.getScheduler().runTaskAsynchronously(main.main, () -> {
                JSONObject data = new JSONObject();
                data.put("action", "bans");

                JSONObject params = JSON.parseObject(JSON.toJSONString(
                        new Bans(
                                entry.getType(),
                                Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(entry.getUuid()))).getName(),
                                entry.getReason(),
                                entry.getDateStart(),
                                entry.getDateEnd(),
                                entry.getDateEnd() == 0
                        )
                ));
                data.put("params", params);

                send(data.toJSONString());
            });
        }
    };

    public static void registerLiteBansListener() {
        Events.get().register(listener);
    }

    public static void unregisterLiteBansListener() {
        try {
            Events.get().unregister(listener);
        } catch (Exception ignored) {
        }
    }
}
