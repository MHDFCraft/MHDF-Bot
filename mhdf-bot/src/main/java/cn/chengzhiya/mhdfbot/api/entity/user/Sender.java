package cn.chengzhiya.mhdfbot.api.entity.user;

import cn.chengzhiya.mhdfbot.api.enums.user.RoleType;
import cn.chengzhiya.mhdfbot.api.enums.user.SexType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Sender {
    private final Long userId;
    private final String nickName;
    private final String card;
    private final String longNick;
    private final SexType sex;
    private final Integer age;
    private final String area;
    private final String level;
    private final RoleType role;
    private final String title;

    public Sender(JSONObject data) {
        this.userId = data.getLong("user_id");
        this.nickName = data.getString("nickname");
        this.card = data.getString("card");
        this.longNick = data.getString("longNick");
        this.sex = SexType.get(data.getString("sex"));
        this.age = data.getInteger("age");
        this.area = data.getString("area");
        this.level = data.getString("level");
        this.role = RoleType.get(data.getString("role"));
        this.title = data.getString("title");
    }
}
