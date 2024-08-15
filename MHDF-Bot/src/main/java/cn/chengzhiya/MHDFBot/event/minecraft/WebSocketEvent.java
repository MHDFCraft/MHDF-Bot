package cn.ChengZhiYa.MHDFBot.event.minecraft;

import cn.ChengZhiYa.MHDFBot.api.enums.minecraft.ServerType;
import cn.ChengZhiYa.MHDFBot.api.event.MinecraftEvent;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class WebSocketEvent extends MinecraftEvent {
    ServerType serverType;
    JSONObject data;

    public WebSocketEvent(ServerType serverType,JSONObject eventData) {
        this.serverType = serverType;
        this.data = eventData;
    }
}
