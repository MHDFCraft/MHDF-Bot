package cn.chengzhiya.mhdfbot.api.enums.notice;

public enum GroupIncreaseSubType {
    APPROVE,
    INVITE,
    OTHER;

    public static GroupIncreaseSubType get(String subType) {
        return subType != null ? switch (subType) {
            case "approve" -> APPROVE;
            case "invite" -> INVITE;
            default -> OTHER;
        } : OTHER;
    }
}
