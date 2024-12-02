package cn.chengzhiya.mhdfbot.api.event.message;

import cn.chengzhiya.mhdfbot.api.enums.user.RoleType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupMessageEvent extends AbstractMessageEvent {
    private final Long groupId;
    private final RoleType senderRole;

    public GroupMessageEvent(JSONObject data) {
        super(data);
        this.groupId = data.getLong("group_id");
        this.senderRole = RoleType.get(data.getJSONObject("sender").getString("role"));
    }
}
