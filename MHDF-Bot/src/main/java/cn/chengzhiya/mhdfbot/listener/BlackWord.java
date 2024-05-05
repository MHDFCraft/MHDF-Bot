package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbot.util.Util;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.ignore.SensitiveWordCharIgnores;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import com.github.houbb.sensitive.word.support.tag.WordTags;
import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static cn.chengzhiya.mhdfbot.util.Util.ifContainsBlackWord;

@Shiro
@Component
public final class BlackWord {
    @GroupMessageHandler
    public void onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (ifContainsBlackWord(event.getMessage())) {
            bot.deleteMsg(event.getMessageId());
            if (Util.getConfig().getInt("BlackWordSettings.Mute") != -1) {
                bot.setGroupBan(event.getGroupId(), event.getUserId(), 60 * Util.getConfig().getInt("BlackWordSettings.Mute"));
            }
        }
    }
}