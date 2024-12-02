package cn.chengzhiya.mhdfbot.api.enums.notice;

public enum NoticeType {
    GROUP_UPLOAD,
    GROUP_ADMIN,
    GROUP_DECREASE,
    GROUP_INCREASE,
    GROUP_BAN,
    GROUP_CARD,
    GROUP_RECALL,
    FRIEND_RECALL,
    FRIEND_ADD,
    NOTIFY;

    public static NoticeType get(String type) {
        return type != null ? switch (type) {
            case "group_upload" -> GROUP_UPLOAD;
            case "group_admin" -> GROUP_ADMIN;
            case "group_decrease" -> GROUP_DECREASE;
            case "group_increase" -> GROUP_INCREASE;
            case "group_ban" -> GROUP_BAN;
            case "group_card" -> GROUP_CARD;
            case "group_recall" -> GROUP_RECALL;
            case "friend_add" -> FRIEND_ADD;
            case "friend_recall" -> FRIEND_RECALL;
            default -> NOTIFY;
        } : NOTIFY;
    }
}
