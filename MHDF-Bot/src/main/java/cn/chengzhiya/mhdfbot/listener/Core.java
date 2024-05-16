package cn.chengzhiya.mhdfbot.listener;

import cn.chengzhiya.mhdfbot.MHDFBot;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.CoreEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public final class Core extends CoreEvent {
    @Override
    public void online(Bot bot) {
        MHDFBot.bot = bot;
    }

    @Override
    public void offline(long account) {
        MHDFBot.bot = null;
    }
}
