package cn.chengzhiya.mhdfbot.api.entity.bot;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class VersionInfo {
    private final String appName;
    private final String appVersion;
    private final String protocolVersion;

    public VersionInfo(JSONObject data) {
        this.appName = data.getString("app_name");
        this.appVersion = data.getString("app_version");
        this.protocolVersion = data.getString("protocol_version");
    }
}
