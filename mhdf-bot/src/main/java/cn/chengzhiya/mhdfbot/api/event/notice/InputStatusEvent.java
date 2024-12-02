package cn.chengzhiya.mhdfbot.api.event.notice;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class InputStatusEvent extends AbstractNoticeEvent {
    private final String statusText;
    private final Integer eventType;

    public InputStatusEvent(JSONObject data) {
        super(data);
        this.statusText = data.getString("status_text");
        this.eventType = data.getInteger("event_type");
    }
}
