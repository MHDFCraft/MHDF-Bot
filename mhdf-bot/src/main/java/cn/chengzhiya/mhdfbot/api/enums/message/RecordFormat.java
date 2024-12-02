package cn.chengzhiya.mhdfbot.api.enums.message;

public enum RecordFormat {
    MP3,
    AMR,
    WMA,
    M4A,
    SPX,
    OGG,
    WAV,
    FLAC,
    OTHER;

    public static RecordFormat get(String subType) {
        return subType != null ? switch (subType) {
            case "mp3" -> MP3;
            case "amr" -> AMR;
            case "wma" -> WMA;
            case "m4a" -> M4A;
            case "spx" -> SPX;
            case "ogg" -> OGG;
            case "wav" -> WAV;
            case "flac" -> FLAC;
            default -> OTHER;
        } : OTHER;
    }
}
