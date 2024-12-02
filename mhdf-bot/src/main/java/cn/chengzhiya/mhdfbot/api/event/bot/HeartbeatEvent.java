package cn.chengzhiya.mhdfbot.api.event.bot;

import cn.chengzhiya.mhdfbot.api.event.AbstractEvent;
import com.alibaba.fastjson2.JSONObject;

public final class HeartbeatEvent extends AbstractEvent {
    public HeartbeatEvent(JSONObject data) {
        super(data);
    }
}
