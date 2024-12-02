package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.enums.notice.GroupAdminSubType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupAdminEvent extends AbstractNoticeEvent {
    private final GroupAdminSubType subType;
    private final Long groupId;

    public GroupAdminEvent(JSONObject data) {
        super(data);
        this.subType = GroupAdminSubType.get(data.getString("subType"));
        this.groupId = data.getLong("group_id");
    }
}
