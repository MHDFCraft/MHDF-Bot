package cn.chengzhiya.mhdfbot.api.entity.bot;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Status {
    private final Boolean online;
    private final Boolean good;

    public Status(JSONObject data) {
        this.online = data.getBoolean("online");
        this.good = data.getBoolean("good");
    }
}
