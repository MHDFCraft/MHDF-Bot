package cn.chengzhiya.mhdfbot.api.enums.request;

public enum RequestSubType {
    ADD,
    INVITE,
    OTHER;

    public static RequestSubType get(String subType) {
        return subType != null ? switch (subType) {
            case "add" -> ADD;
            case "invite" -> INVITE;
            default -> OTHER;
        } : OTHER;
    }
}
