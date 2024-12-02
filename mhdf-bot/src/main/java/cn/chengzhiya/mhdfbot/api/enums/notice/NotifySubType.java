package cn.chengzhiya.mhdfbot.api.enums.notice;


public enum NotifySubType {
    POKE,
    LUCKY_KING,
    HONOR,
    INPUT_STATUS,
    OTHER;

    public static NotifySubType get(String subType) {
        return subType != null ? switch (subType) {
            case "poke" -> POKE;
            case "lucky_king" -> LUCKY_KING;
            case "honor" -> HONOR;
            case "input_status" -> INPUT_STATUS;
            default -> OTHER;
        } : OTHER;
    }
}
