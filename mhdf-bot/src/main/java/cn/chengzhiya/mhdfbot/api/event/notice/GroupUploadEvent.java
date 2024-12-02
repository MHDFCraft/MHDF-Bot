package cn.chengzhiya.mhdfbot.api.event.notice;

import cn.chengzhiya.mhdfbot.api.entity.File;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupUploadEvent extends AbstractNoticeEvent {
    private final Long groupId;
    private final File file;

    public GroupUploadEvent(JSONObject data) {
        super(data);
        this.groupId = data.getLong("group_id");
        this.file = new File(data.getJSONObject("file"));
    }
}
