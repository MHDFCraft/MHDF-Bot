package cn.ChengZhiYa.mhdfbotapi.entity;

import lombok.Data;

@Data
public final class Marry {
    Long MrQQ;
    Long MrsQQ;

    public Marry(Long MrQQ, Long MrsQQ) {
        this.MrQQ = MrQQ;
        this.MrsQQ = MrsQQ;
    }
}
