package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.enums.notice.HonorType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class HonorEvent extends AbstractNoticeEvent {
    private final HonorType honorType;
    private final Long groupId;
    private final Long targetId;

    public HonorEvent(JSONObject data) {
        super(data);
        this.honorType = HonorType.get(data.getString("honorType"));
        this.groupId = data.getLong("group_id");
        this.targetId = data.getLong("target_id");
    }
}
