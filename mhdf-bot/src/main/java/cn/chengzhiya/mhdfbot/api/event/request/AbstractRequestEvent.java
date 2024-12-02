package cn.chengzhiya.mhdfbot.api.event.request;

import cn.chengzhiya.mhdfbot.api.enums.request.RequestType;
import cn.chengzhiya.mhdfbot.api.event.AbstractEvent;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public abstract class AbstractRequestEvent extends AbstractEvent {
    private final RequestType requestType;
    private final Long userId;
    private final String comment;
    private final String flag;

    public AbstractRequestEvent(JSONObject data) {
        super(data);
        this.requestType = RequestType.get(data.getString("request_type"));
        this.userId = data.getLong("user_id");
        this.comment = data.getString("comment");
        this.flag = data.getString("flag");
    }
}
