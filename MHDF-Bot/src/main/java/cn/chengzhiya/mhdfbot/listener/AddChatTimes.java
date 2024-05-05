package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbot.util.Util;
import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.addChatTimes;

@Shiro
@Component
public final class AddChatTimes {
    @GroupMessageHandler
    public void onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (Util.getConfig().getStringList("AllowUseBotList").contains(event.getGroupId().toString())) {
            addChatTimes(event.getUserId(), 1);
        }
    }
}
