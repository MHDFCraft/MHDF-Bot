package cn.chengzhiya.mhdfbot.api.entity.user;

import cn.chengzhiya.mhdfbot.api.enums.user.SexType;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public abstract class AbstractStranger {
    private final String qid;
    private final Long id;
    private final String nickName;
    private final String card;
    private final Birthday birthday;
    private final SexType sex;
    private final String country;
    private final String province;
    private final String city;
    private final Long regTime;
    private final String[] labels;
    private final Integer level;

    public AbstractStranger(JSONObject data) {
        this.qid = data.getString("qid");
        this.id = data.getLong("user_id");
        this.nickName = data.getString("nickname");
        this.card = data.getString("longNick");
        this.birthday = new Birthday(
                data.getIntValue("birthday_year"),
                data.getIntValue("birthday_month"),
                data.getIntValue("birthday_day")
        );
        this.sex = SexType.get(data.getString("sex"));
        this.country = data.getString("country");
        this.province = data.getString("province");
        this.city = data.getString("city");
        this.regTime = data.getLong("regTime");
        this.labels = data.get("labels") != null ? data.getJSONArray("labels").toArray(String.class) : new String[0];
        this.level = data.getInteger("level");
    }
}
