package cn.ChengZhiYa.MHDFBot.listener;

import cn.ChengZhiYa.MHDFBot.util.Util;
import com.mikuac.shiro.annotation.AnyMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.AnyMessageEvent;
import org.springframework.stereotype.Component;

@Shiro
@Component
public final class AnyMessage {
    @AnyMessageHandler
    public void onAnyMessage(Bot bot, AnyMessageEvent event) {
        String[] args = event.getMessage().split(" ");
        MsgUtils Message = MsgUtils
                .builder()
                .reply(event.getMessageId());
        if (Util.getConfig().getBoolean("ReplayAt") && event.getMessageType().equals("group")) {
            Message.at(event.getUserId());
        }
        Message.text("\n");
        switch (args[0]) {
            case "#重载":
                if (Util.getConfig().getStringList("AllowUseAdminCommandList").contains(event.getUserId().toString())) {
                    reloadConfig();
                    reloadLang();
                    Message.text(i18n("Messages.Reload.ReloadDone"));
                } else {
                    Message.text(i18n("Messages.Reload.NoPermission"));
                }
                bot.sendMsg(event, Message.build(), false);
                break;
            default:
        }
    }
}
