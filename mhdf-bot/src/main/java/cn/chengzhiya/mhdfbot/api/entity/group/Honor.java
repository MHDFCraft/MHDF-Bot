package cn.chengzhiya.mhdfbot.api.entity.group;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Honor {
    private final Long userId;
    private final String avatar;
    private final String nickname;
    private final Integer dayCount;
    private final String description;

    public Honor(JSONObject data) {
        this.userId = data.getLong("user_id");
        this.avatar = data.getString("avatar");
        this.nickname = data.getString("nickname");
        this.dayCount = data.getInteger("day_count");
        this.description = data.getString("description");
    }
}
