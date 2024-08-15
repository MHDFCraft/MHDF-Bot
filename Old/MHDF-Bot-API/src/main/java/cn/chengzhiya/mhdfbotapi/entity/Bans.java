package cn.ChengZhiYa.mhdfbotapi.entity;

import lombok.Data;

@Data
public final class Bans {
    String Type;
    String PlayerName;
    String Reason;
    Long StartTime;
    Long EndTime;
    boolean Temp;

    public Bans(String Type, String playerName, String reason, Long startTime, Long endTime, boolean temp) {
        this.Type = Type;
        this.PlayerName = playerName;
        this.Reason = reason;
        this.StartTime = startTime;
        this.EndTime = endTime;
        this.Temp = temp;
    }
}
