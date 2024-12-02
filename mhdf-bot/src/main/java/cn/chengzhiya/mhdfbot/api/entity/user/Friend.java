package cn.chengzhiya.mhdfbot.api.entity.user;

import cn.chengzhiya.mhdfbot.api.enums.user.SexType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Friend {
    private final Long userId;
    private final String nickName;
    private final String remark;
    private final SexType sex;
    private final Integer level;

    public Friend(JSONObject data) {
        this.userId = data.getLong("user_id");
        this.nickName = data.getString("nickname");
        this.remark = data.getString("remark");
        this.sex = SexType.get(data.getString("sex"));
        this.level = data.getInteger("level");
    }
}
