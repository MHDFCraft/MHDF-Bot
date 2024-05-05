package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbotapi.entity.PlayerData;
import com.mikuac.shiro.annotation.GroupDecreaseHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.notice.GroupDecreaseNoticeEvent;
import org.springframework.stereotype.Component;

import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.getPlayerDataList;
import static cn.chengzhiya.mhdfbotapi.util.DatabaseUtil.unbind;

@Shiro
@Component
public final class GroupDecreaseNotice {
    @GroupDecreaseHandler
    public void onGroupDecreaseNotice(Bot bot, GroupDecreaseNoticeEvent event) {
        for (PlayerData playerData : getPlayerDataList(event.getUserId())) {
            unbind(playerData);
        }
    }
}
