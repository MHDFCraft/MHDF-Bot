package cn.chengzhiya.mhdfbot.api.enums.message;

public enum MessageType {
    GROUP,
    PRIVATE,
    OTHER;

    public static MessageType get(String type) {
        return type != null ? switch (type) {
            case "group" -> GROUP;
            case "private" -> PRIVATE;
            default -> OTHER;
        } : OTHER;
    }
}
