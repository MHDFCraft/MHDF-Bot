package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbot.util.Util;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.chengzhiya.mhdfbot.server.webSocket.send;

@Shiro
@Component
public final class At {
    @GroupMessageHandler
    public void onGroupMessage(Bot bot, GroupMessageEvent event) {
        if (Util.getConfig().getStringList("AllowUseBotList").contains(event.getGroupId().toString())) {
            Pattern pattern = Pattern.compile("\\[CQ:at,qq=(.*?)]");
            Matcher matcher = pattern.matcher(event.getMessage());

            while (matcher.find()) {
                if (!matcher.group(1).equals("all")) {
                    Long atQQ = Long.parseLong(matcher.group(1));

                    JSONObject data = new JSONObject();
                    data.put("action", "atQQ");

                    JSONObject params = new JSONObject();
                    params.put("QQ", atQQ);

                    data.put("params", params);

                    send(data.toJSONString());
                }
            }
        }
    }
}
