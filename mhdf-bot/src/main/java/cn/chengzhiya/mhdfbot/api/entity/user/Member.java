package cn.chengzhiya.mhdfbot.api.entity.user;

import cn.chengzhiya.mhdfbot.api.enums.user.RoleType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Member extends AbstractStranger {
    private final Long groupId;
    private final String card;
    private final RoleType role;
    private final String title;
    private final Integer level;

    public Member(JSONObject data) {
        super(data);
        this.level = data.getInteger("qq_level");
        this.groupId = data.getLong("group_id");
        this.card = data.getString("card");
        this.role = RoleType.get(data.getString("role"));
        this.title = data.getString("title");
    }
}
