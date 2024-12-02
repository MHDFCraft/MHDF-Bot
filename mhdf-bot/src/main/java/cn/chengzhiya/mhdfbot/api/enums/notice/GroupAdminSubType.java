package cn.chengzhiya.mhdfbot.api.enums.notice;

public enum GroupAdminSubType {
    SET,
    UNSET,
    OTHER;

    public static GroupAdminSubType get(String subType) {
        return subType != null ? switch (subType) {
            case "set" -> SET;
            case "unset" -> UNSET;
            default -> OTHER;
        } : OTHER;
    }
}
