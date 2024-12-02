package cn.chengzhiya.mhdfbot.api.entity.bot;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class LoginInfo {
    private final Long userId;
    private final String nickname;

    public LoginInfo(JSONObject data) {
        this.userId = data.getLong("user_id");
        this.nickname = data.getString("nickname");
    }
}
