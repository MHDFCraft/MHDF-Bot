package cn.ChengZhiYa.MHDFBot.task;

import cn.ChengZhiYa.MHDFBot.entity.YamlConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.time.LocalDate;

import static cn.ChengZhiYa.mhdfbotapi.util.DatabaseUtil.clearDayChatTimes;
import static cn.ChengZhiYa.mhdfbotapi.util.DatabaseUtil.clearMarry;

@Controller
@Component
public final class CheckDay {
    @Scheduled(cron = "* * * * * ?")
    public void run() {
        {
            File cacheFile = new File("./", "cache.yml");
            YamlConfiguration cache = YamlConfiguration.loadConfiguration(cacheFile);
            Integer Day = cache.getInt("Day");
            if (Day != null) {
                if (Day != LocalDate.now().getDayOfMonth()) {
                    clearMarry();
                    clearDayChatTimes();
                }
            }
            cache.set("Day", LocalDate.now().getDayOfMonth());
            cache.save(cacheFile);
        }
    }
}