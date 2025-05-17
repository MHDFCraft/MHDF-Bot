//package cn.chengzhiya.mhdfbot.api.bot;
//
//import cn.chengzhiya.mhdfbot.api.entity.bot.LoginInfo;
//import cn.chengzhiya.mhdfbot.api.entity.bot.Status;
//import cn.chengzhiya.mhdfbot.api.entity.bot.VersionInfo;
//import cn.chengzhiya.mhdfbot.api.entity.group.Group;
//import cn.chengzhiya.mhdfbot.api.entity.group.GroupHonor;
//import cn.chengzhiya.mhdfbot.api.entity.message.Record;
//import cn.chengzhiya.mhdfbot.api.entity.user.Friend;
//import cn.chengzhiya.mhdfbot.api.entity.user.Member;
//import cn.chengzhiya.mhdfbot.api.entity.user.Stranger;
//import cn.chengzhiya.mhdfbot.api.enums.message.MessageType;
//import cn.chengzhiya.mhdfbot.api.enums.message.RecordFormat;
//import cn.chengzhiya.mhdfbot.api.enums.notice.HonorType;
//import cn.chengzhiya.mhdfbot.api.enums.request.RequestSubType;
//import cn.chengzhiya.mhdfbot.api.event.message.AbstractMessageEvent;
//
//import java.io.File;
//import java.util.List;
//
//public final class BotQqBotImpl implements Bot{
//    @Override
//    public void cleanCache() {
//
//    }
//
//    @Override
//    public void restart(Long delay) {
//
//    }
//
//    @Override
//    public void restart() {
//
//    }
//
//    @Override
//    public Status getStatus() {
//        return null;
//    }
//
//    @Override
//    public VersionInfo getVersionInfo() {
//        return null;
//    }
//
//    @Override
//    public LoginInfo getLoginInfo() {
//        return null;
//    }
//
//    @Override
//    public Boolean ifCanSendRecord() {
//        return null;
//    }
//
//    @Override
//    public Boolean ifCanSendImage() {
//        return null;
//    }
//
//    @Override
//    public Long getCsrfToken() {
//        return 0;
//    }
//
//    @Override
//    public List<Friend> getFriendList() {
//        return List.of();
//    }
//
//    @Override
//    public List<Group> getGroupList() {
//        return List.of();
//    }
//
//    @Override
//    public AbstractMessageEvent getMsg(Long messageId) {
//        return null;
//    }
//
//    @Override
//    public Long sendMsg(MessageType messageType, Long targetId, String message, boolean autoEscape) {
//        return 0;
//    }
//
//    @Override
//    public Long sendPrivateMsg(Long targetId, String message, boolean autoEscape) {
//        return 0;
//    }
//
//    @Override
//    public Long sendPrivateMsg(Long targetId, String message) {
//        return 0;
//    }
//
//    @Override
//    public Long sendGroupMsg(Long targetId, String message, boolean autoEscape) {
//        return 0;
//    }
//
//    @Override
//    public Long sendGroupMsg(Long targetId, String message) {
//        return 0;
//    }
//
//    @Override
//    public void deleteMsg(Long messageId) {
//
//    }
//
//    @Override
//    public void sendLike(Long targetId, int times) {
//
//    }
//
//    @Override
//    public void groupKick(Long groupId, Long userId, boolean rejectAddRequest) {
//
//    }
//
//    @Override
//    public void groupKick(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void setGroupMute(Long groupId, Long userId, Long duration) {
//
//    }
//
//    @Override
//    public void setGroupMute(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void unsetGroupMute(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void setGroupWholeMute(Long groupId, boolean enable) {
//
//    }
//
//    @Override
//    public void setGroupWholeMute(Long groupId) {
//
//    }
//
//    @Override
//    public void unsetGroupWholeMute(Long groupId) {
//
//    }
//
//    @Override
//    public void setGroupAdmin(Long groupId, Long userId, boolean enable) {
//
//    }
//
//    @Override
//    public void setGroupAdmin(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void unsetGroupAdmin(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void setGroupCard(Long groupId, Long userId, String card) {
//
//    }
//
//    @Override
//    public void unsetGroupCard(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void setGroupName(Long groupId, String name) {
//
//    }
//
//    @Override
//    public void leaveGroup(Long groupId, boolean dismiss) {
//
//    }
//
//    @Override
//    public void leaveGroup(Long groupId) {
//
//    }
//
//    @Override
//    public void dismissGroup(Long groupId) {
//
//    }
//
//    @Override
//    public void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle, Long duration) {
//
//    }
//
//    @Override
//    public void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
//
//    }
//
//    @Override
//    public void unsetGroupSpecialTitle(Long groupId, Long userId) {
//
//    }
//
//    @Override
//    public void handleFriendAddRequest(String flag, boolean approve, String remark) {
//
//    }
//
//    @Override
//    public void handleFriendAddRequest(String flag, boolean approve) {
//
//    }
//
//    @Override
//    public void acceptFriendAddRequest(String flag) {
//
//    }
//
//    @Override
//    public void rejectFriendAddRequest(String flag) {
//
//    }
//
//    @Override
//    public void handleGroupAddRequest(String flag, RequestSubType type, boolean approve, String reason) {
//
//    }
//
//    @Override
//    public void handleGroupAddRequest(String flag, RequestSubType type, boolean approve) {
//
//    }
//
//    @Override
//    public void acceptGroupAddRequest(String flag, RequestSubType type) {
//
//    }
//
//    @Override
//    public void rejectGroupAddRequest(String flag, RequestSubType type) {
//
//    }
//
//    @Override
//    public Stranger getStrangerInfo(Long userId, boolean cache) {
//        return null;
//    }
//
//    @Override
//    public Stranger getStrangerInfo(Long userId) {
//        return null;
//    }
//
//    @Override
//    public Member getGroupMemberInfo(Long groupId, Long userId, boolean cache) {
//        return null;
//    }
//
//    @Override
//    public Member getGroupMemberInfo(Long groupId, Long userId) {
//        return null;
//    }
//
//    @Override
//    public List<Member> getGroupMemberList(Long groupId, boolean cache) {
//        return List.of();
//    }
//
//    @Override
//    public List<Member> getGroupMemberList(Long groupId) {
//        return List.of();
//    }
//
//    @Override
//    public GroupHonor getGroupHonorInfo(Long groupId, HonorType type) {
//        return null;
//    }
//
//    @Override
//    public GroupHonor getGroupHonorInfo(Long groupId) {
//        return null;
//    }
//
//    @Override
//    public String getCookies(String domain) {
//        return "";
//    }
//
//    @Override
//    public Record getRecord(String file, RecordFormat format) {
//        return null;
//    }
//
//    @Override
//    public Record getRecord(String file) {
//        return null;
//    }
//
//    @Override
//    public File getImage(String file) {
//        return null;
//    }
//}
