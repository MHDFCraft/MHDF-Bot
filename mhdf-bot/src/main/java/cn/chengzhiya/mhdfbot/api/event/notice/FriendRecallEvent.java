package cn.chengzhiya.mhdfbot.api.event.notice;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class FriendRecallEvent extends AbstractNoticeEvent {
    private final Long messageId;

    public FriendRecallEvent(JSONObject data) {
        super(data);
        this.messageId = data.getLong("message_id");
    }
}
