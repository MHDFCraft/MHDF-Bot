package cn.chengzhiya.mhdfbot.api.event.bot;

import cn.chengzhiya.mhdfbot.api.enums.bot.LifecycleSubType;
import cn.chengzhiya.mhdfbot.api.event.AbstractEvent;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class LifecycleEvent extends AbstractEvent {
    private final LifecycleSubType subType;

    public LifecycleEvent(JSONObject data) {
        super(data);
        this.subType = LifecycleSubType.get(data.getString("sub_type"));
    }
}
