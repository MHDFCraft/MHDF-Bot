package cn.chengzhiya.mhdfbot.api.util;

@SuppressWarnings("unused")
public final class MessageUtil {
    /**
     * 转义文本
     *
     * @param string 需要转义的文本
     * @return 转义后的文本
     */
    public static String escape(String string) {
        return string.replace("&", "&amp;")
                .replace(",", "&#44;")
                .replace("[", "&#91;")
                .replace("]", "&#93;");
    }

    /**
     * 去除转义文本
     *
     * @param string 经过转义的文本
     * @return 转义前的文本
     */
    public static String unescape(String string) {
        return string.replace("&amp;", "&")
                .replace("&#44;", ",")
                .replace("&#91;", "[")
                .replace("&#93;", "]");
    }
}
