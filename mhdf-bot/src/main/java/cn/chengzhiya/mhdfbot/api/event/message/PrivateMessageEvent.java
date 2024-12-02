package cn.chengzhiya.mhdfbot.api.event.message;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class PrivateMessageEvent extends AbstractMessageEvent {
    public PrivateMessageEvent(JSONObject data) {
        super(data);
    }
}
