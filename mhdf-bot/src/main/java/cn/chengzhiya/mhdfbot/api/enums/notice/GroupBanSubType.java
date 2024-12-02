package cn.chengzhiya.mhdfbot.api.enums.notice;

public enum GroupBanSubType {
    BAN,
    LIFT_BAN,
    OTHER;

    public static GroupBanSubType get(String subType) {
        return subType != null ? switch (subType) {
            case "ban" -> BAN;
            case "lift_ban" -> LIFT_BAN;
            default -> OTHER;
        } : OTHER;
    }
}
