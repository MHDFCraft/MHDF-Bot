package cn.chengzhiya.mhdfbot.api.enums.bot;

public enum LifecycleSubType {
    CONNECT,
    ENABLE,
    DISABLE,
    OTHER;

    public static LifecycleSubType get(String subType) {
        return subType != null ? switch (subType) {
            case "connect" -> CONNECT;
            case "enable" -> ENABLE;
            case "disable" -> DISABLE;
            default -> OTHER;
        } : OTHER;
    }
}
