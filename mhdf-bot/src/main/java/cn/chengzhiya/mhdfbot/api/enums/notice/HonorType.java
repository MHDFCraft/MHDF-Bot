package cn.chengzhiya.mhdfbot.api.enums.notice;

public enum HonorType {
    TALKATIVE,
    PERFORMER,
    LEGEND,
    STRONG_NEWBIE,
    EMOTION,
    ALL,
    OTHER;

    public static HonorType get(String subType) {
        return subType != null ? switch (subType) {
            case "talkative" -> TALKATIVE;
            case "performer" -> PERFORMER;
            case "legend" -> LEGEND;
            case "strong_newbie" -> STRONG_NEWBIE;
            case "emotion" -> EMOTION;
            case "all" -> ALL;
            default -> OTHER;
        } : OTHER;
    }
}
