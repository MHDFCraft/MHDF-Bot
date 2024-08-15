package cn.ChengZhiYa.MHDFBot.task;

import cn.ChengZhiYa.MHDFBot.util.Util;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFBot.MHDFBot.bot;
import static cn.ChengZhiYa.MHDFBot.util.MessageUtil.getMessage;

@Controller
@Component
public final class TimeMessage {
    public HashMap<String, Integer> messageTimeHashMap = new HashMap<>();

    @Scheduled(cron = "* * * * * ?")
    public void run() {
        for (String autoChatKeys : Objects.requireNonNull(Util.getConfig().getConfigurationSection("AutoChat.WordList")).getKeys()) {
            switch (Util.getConfig().getString("AutoChat.WordList." + autoChatKeys + ".Type")) {
                case "定点发送": {
                    String[] time = Util.getConfig().getString("AutoChat.WordList." + autoChatKeys + ".Time").split(":");
                    if (LocalTime.now().getHour() == Integer.parseInt(time[0]) && LocalTime.now().getMinute() == Integer.parseInt(time[1]) && LocalTime.now().getSecond() == Integer.parseInt(time[2])) {
                        String message = getMessage(Util.getConfig().getStringList("AutoChat.WordList." + autoChatKeys + ".Message"));
                        for (String group : Util.getConfig().getStringList("AutoChat.WordList." + autoChatKeys + ".Group")) {
                            bot.sendGroupMsg(Long.parseLong(group), message, false);
                        }
                    }
                    break;
                }
                case "定时发送": {
                    int delay = Util.getConfig().getInt("AutoChat.WordList." + autoChatKeys + ".Delay");
                    int time = messageTimeHashMap.get(autoChatKeys) != null ? messageTimeHashMap.get(autoChatKeys) : 0;
                    if (delay <= time) {
                        messageTimeHashMap.remove(autoChatKeys);
                        String message = getMessage(Util.getConfig().getStringList("AutoChat.WordList." + autoChatKeys + ".Message"));
                        for (String group : Util.getConfig().getStringList("AutoChat.WordList." + autoChatKeys + ".Group")) {
                            bot.sendGroupMsg(Long.parseLong(group), message, false);
                        }
                    } else {
                        time++;
                        messageTimeHashMap.put(autoChatKeys, time);
                    }
                    break;
                }
            }
        }
    }
}
