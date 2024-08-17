package cn.ChengZhiYa.MHDFBot.api.util;

public final class MessageUtil {
    public static String escape(String string) {
        return string.replace("&", "&amp;")
                .replace(",", "&#44;")
                .replace("[", "&#91;")
                .replace("]", "&#93;");
    }
}
