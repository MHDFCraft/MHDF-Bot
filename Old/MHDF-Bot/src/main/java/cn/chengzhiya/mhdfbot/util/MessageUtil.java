package cn.ChengZhiYa.MHDFBot.util;

import com.mikuac.shiro.common.utils.MsgUtils;

import java.util.List;

public final class MessageUtil {
    public static String getMessage(List<String> messageList) {
        MsgUtils messageBuilder = MsgUtils.builder();
        for (String messages : messageList) {
            String[] message = messages.split("\\|");
            switch (message[0]) {
                case "[Image]":
                    messageBuilder.img(message[1]);
                    break;
                case "[Message]":
                    messageBuilder.text(message[1]);
                    break;
            }
        }
        return messageBuilder.build();
    }
}
