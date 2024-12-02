package cn.chengzhiya.mhdfbot.api.event.notice;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupRecallEvent extends AbstractNoticeEvent {
    private final Long groupId;
    private final Long operatorId;
    private final Long messageId;

    public GroupRecallEvent(JSONObject data) {
        super(data);
        this.groupId = data.getLong("group_id");
        this.operatorId = data.getLong("operator_id");
        this.messageId = data.getLong("message_id");
    }
}
