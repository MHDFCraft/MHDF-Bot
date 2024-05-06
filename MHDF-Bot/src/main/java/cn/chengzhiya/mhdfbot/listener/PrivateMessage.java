package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbot.util.Util;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import org.springframework.stereotype.Component;

import static cn.chengzhiya.mhdfbot.util.Util.*;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.changePassword;

@Shiro
@Component
public final class PrivateMessage {
    @PrivateMessageHandler
    public void onPrivateMessage(Bot bot, PrivateMessageEvent event) {
        if (getConfig().getBoolean("LoginSystemDatabaseSettings.isMHDFLogin")) {
            if (Util.getChangePasswordHashMap().get(event.getUserId()) != null) {
                changePassword(Util.getChangePasswordHashMap().get(event.getUserId()), sha256(event.getMessage()));
                MsgUtils Message = MsgUtils
                        .builder()
                        .reply(event.getMessageId())
                        .text(i18n("Messages.ChangePassword.ChangeDone").replaceAll("\\{Player}", Util.getChangePasswordHashMap().get(event.getUserId())).replaceAll("\\{QQ}", String.valueOf(event.getUserId())).replaceAll("\\{Password}", event.getMessage()));
                bot.sendPrivateMsg(event.getUserId(), Message.build(), false);
            }
        }
    }
}
