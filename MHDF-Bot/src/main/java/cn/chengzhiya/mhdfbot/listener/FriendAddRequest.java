package cn.chengzhiya.mhdfbot.listener;

import com.mikuac.shiro.annotation.FriendAddRequestHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.request.FriendAddRequestEvent;
import org.springframework.stereotype.Component;

@Shiro
@Component
public final class FriendAddRequest {
    @FriendAddRequestHandler
    public void onFriendAddRequest(Bot bot, FriendAddRequestEvent event) {
        bot.setFriendAddRequest(event.getFlag(), true, "");
    }
}
