package cn.chengzhiya.mhdfbot.api.event.message;

import cn.chengzhiya.mhdfbot.api.entity.user.Sender;
import cn.chengzhiya.mhdfbot.api.enums.message.MessageSubType;
import cn.chengzhiya.mhdfbot.api.enums.message.MessageType;
import cn.chengzhiya.mhdfbot.api.event.AbstractEvent;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public abstract class AbstractMessageEvent extends AbstractEvent {
    private final MessageType messageType;
    private final MessageSubType subType;
    private final String message;
    private final Integer messageId;
    private final Sender sender;

    public AbstractMessageEvent(JSONObject data) {
        super(data);
        this.messageType = MessageType.get(data.getString("message_type"));
        this.subType = MessageSubType.get(data.getString("sub_type"));
        this.message = data.getString("raw_message");
        this.messageId = data.getIntValue("message_id");
        this.sender = new Sender(data.getJSONObject("sender"));
    }
}
