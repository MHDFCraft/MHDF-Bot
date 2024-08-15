package cn.ChengZhiYa.MHDFBot.task;

import cn.ChengZhiYa.MHDFBot.util.Util;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.Random;

import static cn.ChengZhiYa.MHDFBot.server.webSocket.send;
import static cn.ChengZhiYa.MHDFBot.util.Util.getVerifyCode;

@Controller
@Component
public final class UpdateVerifyCode {
    @Scheduled(cron = "0 * * * * ?")
    public void run() {
        for (String keys : Util.getVerifyCodeHashMap().keySet()) {
            Random random = new Random();
            Util.getVerifyCodeHashMap().put(keys, random.nextInt(100000, 999999));

            JSONObject data = new JSONObject();
            data.put("action", "getVerifyCode");

            JSONObject params = new JSONObject();
            params.put("playerName", keys);
            params.put("code", getVerifyCode(keys));

            data.put("params", params);

            send(data.toJSONString());
        }
    }
}