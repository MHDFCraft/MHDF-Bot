package cn.chengzhiya.mhdfbot.api.event.request;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.enums.request.RequestSubType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class GroupRequestEvent extends AbstractRequestEvent {
    private final RequestSubType subType;
    private final Long groupId;

    public GroupRequestEvent(JSONObject data) {
        super(data);
        subType = RequestSubType.get(data.getString("sub_type"));
        groupId = data.getLong("group_id");
    }

    public void handlingGroupRequest(boolean accept) {
        MHDFBot.handleGroupAddRequest(getFlag(), getSubType(), accept);
    }
}
