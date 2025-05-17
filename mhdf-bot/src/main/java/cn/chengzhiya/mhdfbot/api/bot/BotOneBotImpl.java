package cn.chengzhiya.mhdfbot.api.bot;

import cn.chengzhiya.mhdfbot.api.entity.bot.LoginInfo;
import cn.chengzhiya.mhdfbot.api.entity.bot.Status;
import cn.chengzhiya.mhdfbot.api.entity.bot.VersionInfo;
import cn.chengzhiya.mhdfbot.api.entity.group.Group;
import cn.chengzhiya.mhdfbot.api.entity.group.GroupHonor;
import cn.chengzhiya.mhdfbot.api.entity.message.Record;
import cn.chengzhiya.mhdfbot.api.entity.user.Friend;
import cn.chengzhiya.mhdfbot.api.entity.user.Member;
import cn.chengzhiya.mhdfbot.api.entity.user.Stranger;
import cn.chengzhiya.mhdfbot.api.enums.message.MessageType;
import cn.chengzhiya.mhdfbot.api.enums.message.RecordFormat;
import cn.chengzhiya.mhdfbot.api.enums.notice.HonorType;
import cn.chengzhiya.mhdfbot.api.enums.request.RequestSubType;
import cn.chengzhiya.mhdfbot.api.event.message.AbstractMessageEvent;
import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.event.message.PrivateMessageEvent;
import cn.chengzhiya.mhdfbot.onebot.OneBotHttpClient;
import cn.chengzhiya.mhdfbot.onebot.OneBotWebSocketClient;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class BotOneBotImpl implements Bot {
    @Getter
    private static final OneBotWebSocketClient oneBotWebSocketClient = new OneBotWebSocketClient();
    @Getter
    private static final OneBotHttpClient oneBotHttpClient = new OneBotHttpClient();

    @Override
    public void cleanCache() {
        getOneBotHttpClient().post("clean_cache");
    }

    @Override
    public void restart(Long delay) {
        JSONObject data = new JSONObject();
        data.put("delay", delay);

        getOneBotHttpClient().post("set_restart", data.toString());
    }

    @Override
    public void restart() {
        restart(0L);
    }

    @Override
    public Status getStatus() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_status"));

        return new Status(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public VersionInfo getVersionInfo() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_version_info"));

        return new VersionInfo(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public LoginInfo getLoginInfo() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_login_info"));

        return new LoginInfo(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public Boolean ifCanSendRecord() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("if_can_send_record"));

        return Objects.requireNonNull(returnData).getJSONObject("data").getBoolean("yes");
    }

    @Override
    public Boolean ifCanSendImage() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("if_can_send_image"));

        return Objects.requireNonNull(returnData).getJSONObject("data").getBoolean("yes");
    }

    @Override
    public Long getCsrfToken() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_csrf_token"));

        return Objects.requireNonNull(returnData).getLong("token");
    }

    @Override
    public List<Friend> getFriendList() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_friend_list"));

        return Objects.requireNonNull(returnData).getList("data", JSONObject.class).stream()
                .map(Friend::new)
                .toList();
    }

    @Override
    public List<Group> getGroupList() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_list"));

        return Objects.requireNonNull(returnData).getList("data", JSONObject.class).stream()
                .map(Group::new)
                .toList();
    }

    @Override
    public AbstractMessageEvent getMsg(Long messageId) {
        JSONObject data = new JSONObject();
        data.put("message_id", messageId);

        JSONObject returnData = Objects.requireNonNull(JSONObject.parseObject(getOneBotHttpClient().post("get_msg", data.toString())))
                .getJSONObject("data");

        return switch (returnData.getString("message_type")) {
            case "group" -> new GroupMessageEvent(returnData);
            case "private" -> new PrivateMessageEvent(returnData);
            default -> null;
        };
    }

    @Override
    public Long sendMsg(MessageType messageType, Long targetId, String message, boolean autoEscape) {
        JSONObject data = new JSONObject();
        if (messageType == MessageType.GROUP) {
            data.put("message_type", "group");
            data.put("group_id", targetId);
        }
        if (messageType == MessageType.PRIVATE) {
            data.put("message_type", "private");
            data.put("user_id", targetId);
        }
        data.put("message", message);
        data.put("auto_escape", autoEscape);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("send_msg", data.toString()));
        return Objects.requireNonNull(returnData).getLong("message_id");
    }

    @Override
    public Long sendPrivateMsg(Long targetId, String message, boolean autoEscape) {
        return sendMsg(MessageType.GROUP, targetId, message, autoEscape);
    }

    @Override
    public Long sendPrivateMsg(Long targetId, String message) {
        return sendPrivateMsg(targetId, message, false);
    }

    @Override
    public Long sendGroupMsg(Long targetId, String message, boolean autoEscape) {
        return sendMsg(MessageType.GROUP, targetId, message, autoEscape);
    }

    @Override
    public Long sendGroupMsg(Long targetId, String message) {
        return sendGroupMsg(targetId, message, false);
    }

    @Override
    public void deleteMsg(Long messageId) {
        JSONObject data = new JSONObject();
        data.put("message_id", messageId);

        getOneBotHttpClient().post("delete_msg", data.toString());
    }

    @Override
    public void sendLike(Long targetId, int times) {
        JSONObject data = new JSONObject();
        data.put("user_id", targetId);
        data.put("times", times);

        getOneBotHttpClient().post("send_like", data.toString());
    }

    @Override
    public void groupKick(Long groupId, Long userId, boolean rejectAddRequest) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("reject_add_request", rejectAddRequest);

        getOneBotHttpClient().post("set_group_kick", data.toString());
    }

    @Override
    public void groupKick(Long groupId, Long userId) {
        groupKick(groupId, userId, false);
    }

    @Override
    public void setGroupMute(Long groupId, Long userId, Long duration) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("duration", duration);

        getOneBotHttpClient().post("set_group_ban", data.toString());
    }

    @Override
    public void setGroupMute(Long groupId, Long userId) {
        setGroupMute(groupId, userId, 1800L);
    }

    @Override
    public void unsetGroupMute(Long groupId, Long userId) {
        setGroupMute(groupId, userId, 0L);
    }

    @Override
    public void setGroupWholeMute(Long groupId, boolean enable) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("enable", enable);

        getOneBotHttpClient().post("set_group_whole_ban", data.toString());
    }

    @Override
    public void setGroupWholeMute(Long groupId) {
        setGroupWholeMute(groupId, true);
    }

    @Override
    public void unsetGroupWholeMute(Long groupId) {
        setGroupWholeMute(groupId, false);
    }

    @Override
    public void setGroupAdmin(Long groupId, Long userId, boolean enable) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("enable", enable);

        getOneBotHttpClient().post("set_group_admin", data.toString());
    }

    @Override
    public void setGroupAdmin(Long groupId, Long userId) {
        setGroupAdmin(groupId, userId, true);
    }

    @Override
    public void unsetGroupAdmin(Long groupId, Long userId) {
        setGroupAdmin(groupId, userId, false);
    }

    @Override
    public void setGroupCard(Long groupId, Long userId, String card) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("card", card);

        getOneBotHttpClient().post("set_group_card", data.toString());
    }

    @Override
    public void unsetGroupCard(Long groupId, Long userId) {
        setGroupCard(groupId, userId, null);
    }

    @Override
    public void setGroupName(Long groupId, String name) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("group_name", name);

        getOneBotHttpClient().post("set_group_name", data.toString());
    }

    @Override
    public void leaveGroup(Long groupId, boolean dismiss) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("is_dismiss", dismiss);

        getOneBotHttpClient().post("set_group_leave", data.toString());
    }

    @Override
    public void leaveGroup(Long groupId) {
        leaveGroup(groupId, false);
    }

    @Override
    public void dismissGroup(Long groupId) {
        leaveGroup(groupId, true);
    }

    @Override
    public void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle, Long duration) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("special_title", specialTitle);
        data.put("duration", duration);

        getOneBotHttpClient().post("set_group_special_title", data.toString());
    }

    @Override
    public void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
        setGroupSpecialTitle(groupId, userId, specialTitle, -1L);
    }

    @Override
    public void unsetGroupSpecialTitle(Long groupId, Long userId) {
        setGroupSpecialTitle(groupId, userId, null);
    }

    @Override
    public void handleFriendAddRequest(String flag, boolean approve, String remark) {
        JSONObject data = new JSONObject();
        data.put("flag", flag);
        data.put("approve", approve);
        data.put("remark", remark);

        getOneBotHttpClient().post("set_friend_add_request", data.toString());
    }

    @Override
    public void handleFriendAddRequest(String flag, boolean approve) {
        handleFriendAddRequest(flag, approve, null);
    }

    @Override
    public void acceptFriendAddRequest(String flag) {
        handleFriendAddRequest(flag, true);
    }

    @Override
    public void rejectFriendAddRequest(String flag) {
        handleFriendAddRequest(flag, false);
    }

    @Override
    public void handleGroupAddRequest(String flag, RequestSubType type, boolean approve, String reason) {
        JSONObject data = new JSONObject();
        data.put("flag", flag);
        data.put("type", type.toString().toLowerCase(Locale.ROOT));
        data.put("approve", approve);
        data.put("reason", reason);

        getOneBotHttpClient().post("set_group_add_request", data.toString());
    }

    @Override
    public void handleGroupAddRequest(String flag, RequestSubType type, boolean approve) {
        handleGroupAddRequest(flag, type, approve, null);
    }

    @Override
    public void acceptGroupAddRequest(String flag, RequestSubType type) {
        handleGroupAddRequest(flag, type, true);
    }

    @Override
    public void rejectGroupAddRequest(String flag, RequestSubType type) {
        handleGroupAddRequest(flag, type, false);
    }

    @Override
    public Stranger getStrangerInfo(Long userId, boolean cache) {
        JSONObject data = new JSONObject();
        data.put("user_id", userId);
        data.put("no_cache", !cache);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_stranger_info", data.toString()));

        return new Stranger(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public Stranger getStrangerInfo(Long userId) {
        return getStrangerInfo(userId, true);
    }

    @Override
    public Member getGroupMemberInfo(Long groupId, Long userId, boolean cache) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("no_cache", !cache);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_member_info", data.toString()));

        return new Member(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public Member getGroupMemberInfo(Long groupId, Long userId) {
        return getGroupMemberInfo(groupId, userId, true);
    }

    @Override
    public List<Member> getGroupMemberList(Long groupId, boolean cache) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("no_cache", !cache);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_member_list", data.toString()));

        return Objects.requireNonNull(returnData).getList("data", JSONObject.class).stream()
                .map(Member::new)
                .toList();
    }

    @Override
    public List<Member> getGroupMemberList(Long groupId) {
        return getGroupMemberList(groupId, true);
    }

    @Override
    public GroupHonor getGroupHonorInfo(Long groupId, HonorType type) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("type", type.toString().toLowerCase(Locale.ROOT));

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_honor_info", data.toString()));

        return new GroupHonor(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public GroupHonor getGroupHonorInfo(Long groupId) {
        return getGroupHonorInfo(groupId, HonorType.ALL);
    }

    @Override
    public String getCookies(String domain) {
        JSONObject data = new JSONObject();
        data.put("domain", domain);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_cookies", data.toString()));

        return Objects.requireNonNull(returnData).getString("cookies");
    }

    @Override
    public Record getRecord(String file, RecordFormat format) {
        JSONObject data = new JSONObject();
        data.put("file", file);
        data.put("format", format.toString());

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_record", data.toString()));

        return new Record(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    @Override
    public Record getRecord(String file) {
        return getRecord(file, RecordFormat.WAV);
    }

    @Override
    public File getImage(String file) {
        JSONObject data = new JSONObject();
        data.put("file", file);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_image", data.toString()));

        return new java.io.File(Objects.requireNonNull(returnData).getJSONObject("data").getString("file"));
    }
}
