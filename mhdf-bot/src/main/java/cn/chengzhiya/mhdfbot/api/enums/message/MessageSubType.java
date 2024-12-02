package cn.chengzhiya.mhdfbot.api.enums.message;

public enum MessageSubType {
    FRIEND,
    GROUP,
    NORMAL,
    ANONYMOUS,
    NOTICE,
    OTHER;

    public static MessageSubType get(String subType) {
        return subType != null ? switch (subType) {
            case "friend" -> FRIEND;
            case "group" -> GROUP;
            case "normal" -> NORMAL;
            case "anonymous" -> ANONYMOUS;
            case "notice" -> NOTICE;
            default -> OTHER;
        } : OTHER;
    }
}
