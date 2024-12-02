package cn.chengzhiya.mhdfbot.api.enums.request;

public enum RequestType {
    GROUP,
    FRIEND,
    OTHER;

    public static RequestType get(String type) {
        return type != null ? switch (type) {
            case "group" -> GROUP;
            case "friend" -> FRIEND;
            default -> OTHER;
        } : OTHER;
    }
}
