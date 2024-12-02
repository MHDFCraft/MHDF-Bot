package cn.chengzhiya.mhdfbot.api.entity.group;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Group {
    private final Long groupId;
    private final String groupName;
    private final int memberCount;
    private final int maxMemberCount;

    public Group(JSONObject data) {
        this.groupId = data.getLong("group_id");
        this.groupName = data.getString("group_name");
        this.memberCount = data.getIntValue("member_count");
        this.maxMemberCount = data.getIntValue("max_member_count");
    }
}
