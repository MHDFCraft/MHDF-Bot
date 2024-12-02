package cn.chengzhiya.mhdfbot.api.builder;

import cn.chengzhiya.mhdfbot.api.entity.Music;
import cn.chengzhiya.mhdfbot.api.util.MessageUtil;

@SuppressWarnings("unused")
public final class MessageBuilder {
    private final StringBuilder builder = new StringBuilder();

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder text(String text) {
        builder.append(text);
        return this;
    }

    public MessageBuilder face(int id) {
        builder.append("[CQ:face,id=")
                .append(id)
                .append("]");
        return this;
    }

    public MessageBuilder at(long qq) {
        builder.append("[CQ:at,qq=")
                .append(qq)
                .append("]");
        return this;
    }

    public MessageBuilder atAll() {
        builder.append("[CQ:at,qq=all]");
        return this;
    }

    public MessageBuilder image(String image) {
        builder.append("[CQ:image,")
                .append(MediaBuilder.builder().file(image).build())
                .append("]");
        return this;
    }

    public MessageBuilder flashImage(String image) {
        builder.append("[CQ:image,type=flash,")
                .append(MediaBuilder.builder().file(image).build())
                .append("]");
        return this;
    }

    public MessageBuilder video(String video, String cover) {
        builder.append("[CQ:video,")
                .append(MediaBuilder.builder().file(video).cover(cover).build())
                .append("]");
        return this;
    }

    public MessageBuilder audio(String audio) {
        builder.append("[CQ:record,")
                .append(MediaBuilder.builder().file(audio).build())
                .append("]");
        return this;
    }

    public MessageBuilder tts(String message) {
        builder.append("[CQ:tts,text=")
                .append(message)
                .append("]");
        return this;
    }

    public MessageBuilder reply(int messageId) {
        builder.append("[CQ:reply,id=")
                .append(messageId)
                .append("]");
        return this;
    }

    public MessageBuilder music(String type, long musicId) {
        builder.append("[CQ:music,type=")
                .append(type)
                .append(",id=")
                .append(musicId)
                .append("]");
        return this;
    }

    public MessageBuilder customMusic(Music music) {
        builder.append("[CQ:music,type=custom,url=")
                .append(MessageUtil.escape(music.url()))
                .append(",title=")
                .append(MessageUtil.escape(music.title()))
                .append(",content=")
                .append(MessageUtil.escape(music.content()))
                .append(",image=")
                .append(MessageUtil.escape(music.image()))
                .append(",audio=")
                .append(MessageUtil.escape(music.audio()))
                .append("]");
        return this;
    }

    public MessageBuilder forward(String forwardId) {
        builder.append("[CQ:forward,id=")
                .append(forwardId)
                .append("]");
        return this;
    }

    public String build() {
        return builder.toString();
    }
}
