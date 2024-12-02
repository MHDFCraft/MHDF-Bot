package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.enums.notice.GroupDecreaseSubType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupDecreaseEvent extends AbstractNoticeEvent {
    private final GroupDecreaseSubType subType;
    private final Long groupId;
    private final Long operatorId;

    public GroupDecreaseEvent(JSONObject data) {
        super(data);
        this.subType = GroupDecreaseSubType.get(data.getString("sub_type"));
        this.groupId = data.getLong("group_id");
        this.operatorId = data.getLong("operator_id");
    }
}

