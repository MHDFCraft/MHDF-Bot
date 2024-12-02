package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.enums.notice.GroupBanSubType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupBanEvent extends AbstractNoticeEvent {
    private final GroupBanSubType subType;
    private final Long groupId;
    private final Long operatorId;
    private final Long duration;

    public GroupBanEvent(JSONObject data) {
        super(data);
        this.subType = GroupBanSubType.get(data.getString("subType"));
        this.groupId = data.getLong("group_id");
        this.operatorId = data.getLong("operator_id");
        this.duration = data.getLong("duration");
    }
}
