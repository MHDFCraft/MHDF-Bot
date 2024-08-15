package cn.ChengZhiYa.MHDFBot.listener;

import cn.ChengZhiYa.MHDFBot.util.Util;
import com.mikuac.shiro.annotation.GroupIncreaseHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.notice.GroupIncreaseNoticeEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFBot.util.MessageUtil.getMessage;

@Shiro
@Component
public final class JoinMessage {
    @GroupIncreaseHandler
    public void onGroupMessage(Bot bot, GroupIncreaseNoticeEvent event) {
        for (String autoChatKeys : Objects.requireNonNull(Util.getConfig().getConfigurationSection("AutoChat.WordList")).getKeys()) {
            if (Util.getConfig().getString("AutoChat.WordList." + autoChatKeys + ".Type").equals("加入群聊")) {
                String message = getMessage(Util.getConfig().getStringList("AutoChat.WordList." + autoChatKeys + ".Message"));
                bot.sendGroupMsg(event.getGroupId(), message, false);
            }
        }
    }
}
