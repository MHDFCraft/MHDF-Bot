package cn.chengzhiya.mhdfbot.api.event.request;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import com.alibaba.fastjson2.JSONObject;

@SuppressWarnings("unused")
public final class FriendRequestEvent extends AbstractRequestEvent {
    public FriendRequestEvent(JSONObject data) {
        super(data);
    }

    public void handlingFriendRequest(boolean accept) {
        MHDFBot.handleFriendAddRequest(getFlag(), accept);
    }
}
