package cn.chengzhiya.mhdfbot.api.event.minecraft;

import cn.chengzhiya.mhdfbot.api.enums.minecraft.ServerType;
import cn.chengzhiya.mhdfbot.api.event.Event;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class MinecraftWebsocketMessageEvent implements Event {
    private final ServerType serverType;
    private final String action;
    private final JSONObject data;

    public MinecraftWebsocketMessageEvent(JSONObject data) {
        this.serverType = ServerType.get(data.getString("server_type"));
        this.action = data.getString("action");
        this.data = data.getJSONObject("data");
    }
}
