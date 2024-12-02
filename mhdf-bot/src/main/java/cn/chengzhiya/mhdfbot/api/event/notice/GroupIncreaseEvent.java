package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.enums.notice.GroupIncreaseSubType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupIncreaseEvent extends AbstractNoticeEvent {
    private final GroupIncreaseSubType subType;
    private final Long groupId;
    private final Long operatorId;

    public GroupIncreaseEvent(JSONObject data) {
        super(data);
        this.subType = GroupIncreaseSubType.get(data.getString("sub_type"));
        this.groupId = data.getLong("group_id");
        this.operatorId = data.getLong("operator_id");
    }
}
