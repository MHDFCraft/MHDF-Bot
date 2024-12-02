package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.event.AbstractEvent;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public abstract class AbstractNoticeEvent extends AbstractEvent {
    private final Long userId;

    public AbstractNoticeEvent(JSONObject data) {
        super(data);
        this.userId = data.getLong("user_id");
    }
}
