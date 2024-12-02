package cn.chengzhiya.mhdfbot.api.entity;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class File {
    private final String id;
    private final String name;
    private final Long size;
    private final Long busid;

    public File(JSONObject data) {
        this.id = data.getString("id");
        this.name = data.getString("nickName");
        this.size = data.getLong("size");
        this.busid = data.getLong("busid");
    }
}
