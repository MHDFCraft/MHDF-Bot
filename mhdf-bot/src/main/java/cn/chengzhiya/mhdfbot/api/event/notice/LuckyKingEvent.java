package cn.chengzhiya.mhdfbot.api.event.notice;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class LuckyKingEvent extends AbstractNoticeEvent {
    private final Long groupId;
    private final Long targetId;

    public LuckyKingEvent(JSONObject data) {
        super(data);
        this.groupId = data.getLong("group_id");
        this.targetId = data.getLong("target_id");
    }
}
