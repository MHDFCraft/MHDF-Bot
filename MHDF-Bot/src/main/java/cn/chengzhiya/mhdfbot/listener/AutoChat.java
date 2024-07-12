package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbot.util.Util;
import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Shiro
@Component
public final class AutoChat {
    @GroupMessageHandler
    public void onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (Util.getConfig().getStringList("AllowUseBotList").contains(event.getGroupId().toString())) {
            for (String autoChatKeys : Objects.requireNonNull(Util.getConfig().getConfigurationSection("AutoChat.WordList")).getKeys()) {
                if (Util.getConfig().getString("AutoChat.WordList." + autoChatKeys + ".Type").equals("检测关键词")) {
                    for (String autoChatWords : Util.getConfig().getStringList("AutoChat.WordList." + autoChatKeys + ".Words")) {
                        if (event.getMessage().contains(autoChatWords)) {
                            MsgUtils Message = MsgUtils
                                    .builder()
                                    .reply(event.getMessageId());
                            if (Util.getConfig().getBoolean("ReplayAt")) {
                                Message.at(event.getUserId());
                            }
                            Message.text("\n");
                            Message.text(Util.getConfig().getString("AutoChat.WordList." + autoChatKeys + ".Replay"));
                            bot.sendGroupMsg(event.getGroupId(), Message.build(), false);
                        }
                    }
                }
            }
        }
    }
}
