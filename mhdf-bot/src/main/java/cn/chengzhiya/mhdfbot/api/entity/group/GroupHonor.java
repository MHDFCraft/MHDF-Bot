package cn.chengzhiya.mhdfbot.api.entity.group;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

import java.util.List;

@Getter
public final class GroupHonor {
    private final Long groupId;
    private final Honor currentTalkative;
    private final List<Honor> talkativeList;
    private final List<Honor> performerList;
    private final List<Honor> legendList;
    private final List<Honor> strongNewbieList;
    private final List<Honor> emotionList;

    public GroupHonor(JSONObject data) {
        this.groupId = data.getLong("group_id");
        this.currentTalkative = new Honor(data.getJSONObject("current_talkative"));
        this.talkativeList = data.getList("talkative_list", Honor.class);
        this.performerList = data.getList("performer_list", Honor.class);
        this.legendList = data.getList("legend_list", Honor.class);
        this.strongNewbieList = data.getList("strong_newbie_list", Honor.class);
        this.emotionList = data.getList("emotion_list", Honor.class);
    }
}
