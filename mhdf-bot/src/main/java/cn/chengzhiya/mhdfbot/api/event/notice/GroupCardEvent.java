package cn.chengzhiya.mhdfbot.api.event.notice;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class GroupCardEvent extends AbstractNoticeEvent {
    private final Long groupId;
    private final String cardNew;
    private final String cardOld;

    public GroupCardEvent(JSONObject data) {
        super(data);
        this.groupId = data.getLong("group_id");
        this.cardNew = data.getString("card_new");
        this.cardOld = data.getString("card_old");
    }
}
