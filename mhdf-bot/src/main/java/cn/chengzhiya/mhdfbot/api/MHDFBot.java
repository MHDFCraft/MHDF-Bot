package cn.chengzhiya.mhdfbot.api;

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
import cn.chengzhiya.mhdfbot.api.manager.CommandManager;
import cn.chengzhiya.mhdfbot.api.manager.ListenerManager;
import cn.chengzhiya.mhdfbot.api.manager.PluginManager;
import cn.chengzhiya.mhdfbot.api.manager.SchedulerManager;
import cn.chengzhiya.mhdfbot.minecraft.MinecraftWebSocketServer;
import cn.chengzhiya.mhdfbot.onebot.OneBotHttpClient;
import cn.chengzhiya.mhdfbot.onebot.OneBotWebSocketClient;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("unused")
public final class MHDFBot {
    @Getter
    private static final Logger logger = getLogger("MHDF-Bot");
    @Getter
    private static final PluginManager pluginManager = new PluginManager();
    @Getter
    private static final CommandManager commandManager = new CommandManager();
    @Getter
    private static final ListenerManager listenerManager = new ListenerManager();
    @Getter
    private static final MinecraftWebSocketServer minecraftWebSocketServer = new MinecraftWebSocketServer();
    @Getter
    private static final OneBotWebSocketClient oneBotWebSocketClient = new OneBotWebSocketClient();
    @Getter
    private static final OneBotHttpClient oneBotHttpClient = new OneBotHttpClient();

    /**
     * 获取调度器实例
     *
     * @return 调度器实例
     */
    public static SchedulerManager getScheduler() {
        return new SchedulerManager();
    }

    /**
     * 获取日志实例
     *
     * @param prefix 日志前缀
     * @return 日志实例
     */
    public static Logger getLogger(String prefix) {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder()
                .setConfigurationName(prefix);

        AppenderComponentBuilder appender = builder.newAppender(prefix, "Console")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "[%d{HH:mm:ss} %p] [" + prefix + "] %msg%n")
                );

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(org.apache.logging.log4j.Level.DEBUG)
                .add(builder.newAppenderRef(prefix));

        builder.add(appender).add(rootLogger);
        Configurator.initialize(builder.build());

        return LogManager.getLogger(prefix);
    }

    /**
     * 发送私聊消息
     *
     * @param targetId 目标QQ号
     * @param message  消息内容
     * @return 消息ID
     */
    public static Long sendPrivateMsg(Long targetId, String message) {
        return sendPrivateMsg(targetId, message, false);
    }

    /**
     * 发送私聊消息
     *
     * @param targetId   目标QQ号
     * @param message    消息内容
     * @param autoEscape 消息内容是否作为纯文本发送
     * @return 消息ID
     */
    public static Long sendPrivateMsg(Long targetId, String message, boolean autoEscape) {
        return sendMsg(MessageType.GROUP, targetId, message, autoEscape);
    }


    /**
     * 发送私聊消息
     *
     * @param targetId 目标群号
     * @param message  消息内容
     * @return 消息ID
     */
    public static Long sendGroupMsg(Long targetId, String message) {
        return sendGroupMsg(targetId, message, false);
    }

    /**
     * 发送私聊消息
     *
     * @param targetId   目标群号
     * @param message    消息内容
     * @param autoEscape 消息内容是否作为纯文本发送
     * @return 消息ID
     */
    public static Long sendGroupMsg(Long targetId, String message, boolean autoEscape) {
        return sendMsg(MessageType.GROUP, targetId, message, autoEscape);
    }

    /**
     * 发送消息
     *
     * @param messageType 目标类型
     * @param targetId    目标QQ号或目标群号
     * @param message     消息内容
     * @param autoEscape  消息内容是否作为纯文本发送
     * @return 消息ID
     */
    public static Long sendMsg(MessageType messageType, Long targetId, String message, boolean autoEscape) {
        JSONObject data = new JSONObject();
        if (messageType == MessageType.GROUP) {
            data.put("message_type", "group");
            data.put("group_id", targetId);
        }
        if (messageType == MessageType.PRIVATE) {
            data.put("message_type", "private");
            data.put("group_id", targetId);
        }
        data.put("message", message);
        data.put("auto_escape", autoEscape);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("send_msg", data.toString()));
        return Objects.requireNonNull(returnData).getLong("message_id");
    }

    /**
     * 通过消息ID撤回消息
     *
     * @param messageId 消息ID
     */
    public static void deleteMsg(Long messageId) {
        JSONObject data = new JSONObject();
        data.put("message_id", messageId);

        getOneBotHttpClient().post("delete_msg", data.toString());
    }

    /**
     * 通过消息ID获取消息事件
     *
     * @param messageId 消息ID
     * @return 消息事件
     */
    public static AbstractMessageEvent getMsg(Long messageId) {
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

    /**
     * 给好友点赞
     *
     * @param targetId 目标QQ号
     * @param times    点赞次数
     */
    public static void sendLike(Long targetId, int times) {
        JSONObject data = new JSONObject();
        data.put("user_id", targetId);
        data.put("times", times);

        getOneBotHttpClient().post("send_like", data.toString());
    }

    /**
     * 踢出指定群聊中的指定群员
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    public static void groupKick(Long groupId, Long userId) {
        groupKick(groupId, userId, false);
    }

    /**
     * 踢出指定群聊中的指定群员
     *
     * @param groupId          目标群号
     * @param userId           目标QQ号
     * @param rejectAddRequest 拒绝此人再次加群请求
     */
    public static void groupKick(Long groupId, Long userId, boolean rejectAddRequest) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("reject_add_request", rejectAddRequest);

        getOneBotHttpClient().post("set_group_kick", data.toString());
    }

    /**
     * 禁言指定群聊中的指定群员30分钟
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    public static void setGroupMute(Long groupId, Long userId) {
        setGroupMute(groupId, userId, 1800L);
    }

    /**
     * 禁言指定群聊中的指定群员
     *
     * @param groupId  目标群号
     * @param userId   目标QQ号
     * @param duration 禁言时长(单位:秒) 0秒为取消禁言
     */
    public static void setGroupMute(Long groupId, Long userId, Long duration) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("duration", duration);

        getOneBotHttpClient().post("set_group_ban", data.toString());
    }

    /**
     * 开启指定群聊全体禁言
     *
     * @param groupId 目标群号
     */
    public static void setGroupWholeMute(Long groupId) {
        setGroupWholeMute(groupId, true);
    }

    /**
     * 设置指定群聊全体禁言
     *
     * @param groupId 目标群号
     * @param enable  是否禁言
     */
    public static void setGroupWholeMute(Long groupId, boolean enable) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("enable", enable);

        getOneBotHttpClient().post("set_group_whole_ban", data.toString());
    }

    /**
     * 设置指定群聊指定用户为群管理
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    public static void setGroupAdmin(Long groupId, Long userId) {
        setGroupAdmin(groupId, userId, true);
    }

    /**
     * 设置指定群聊指定用户的群管理
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @param enable  是否设置
     */
    public static void setGroupAdmin(Long groupId, Long userId, boolean enable) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("enable", enable);

        getOneBotHttpClient().post("set_group_admin", data.toString());
    }

    /**
     * 删除指定群聊指定用户的群昵称
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    public static void setGroupCard(Long groupId, Long userId) {
        setGroupCard(groupId, userId, null);
    }

    /**
     * 设置指定群聊指定用户的群昵称
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @param card    群昵称文本(为空删除群昵称)
     */
    public static void setGroupCard(Long groupId, Long userId, String card) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("card", card);

        getOneBotHttpClient().post("set_group_card", data.toString());
    }

    /**
     * 设置指定群聊的群名称
     *
     * @param groupId 目标群号
     * @param name    群名称文本
     */
    public static void setGroupName(Long groupId, String name) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("group_name", name);

        getOneBotHttpClient().post("set_group_name", data.toString());
    }

    /**
     * 退出指定群聊
     *
     * @param groupId 目标群号
     */
    public static void leaveGroup(Long groupId) {
        leaveGroup(groupId, false);
    }

    /**
     * 退出指定群聊
     *
     * @param groupId 目标群号
     * @param dismiss 是否解散
     */
    public static void leaveGroup(Long groupId, boolean dismiss) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("is_dismiss", dismiss);

        getOneBotHttpClient().post("set_group_leave", data.toString());
    }

    /**
     * 删除指定群聊指定用户的头衔
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    public static void setGroupSpecialTitle(Long groupId, Long userId) {
        setGroupSpecialTitle(groupId, userId, null, -1L);
    }

    /**
     * 设置指定群聊指定用户的头衔
     *
     * @param groupId      目标群号
     * @param userId       目标QQ号
     * @param specialTitle 头衔文本(为空删除头衔)
     */
    public static void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
        setGroupSpecialTitle(groupId, userId, specialTitle, -1L);
    }

    /**
     * 设置指定群聊指定用户的头衔
     *
     * @param groupId      目标群号
     * @param userId       目标QQ号
     * @param specialTitle 头衔文本(为空删除头衔)
     * @param duration     头衔有效期
     */
    public static void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle, Long duration) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("special_title", specialTitle);
        data.put("duration", duration);

        getOneBotHttpClient().post("set_group_special_title", data.toString());
    }

    /**
     * 同意好友请求
     *
     * @param flag 好友请求flag
     */
    public static void handleFriendAddRequest(String flag) {
        handleFriendAddRequest(flag, true, null);
    }

    /**
     * 处理好友请求
     *
     * @param flag    好友请求flag
     * @param approve 是否同意好友请求
     */
    public static void handleFriendAddRequest(String flag, boolean approve) {
        handleFriendAddRequest(flag, approve, null);
    }

    /**
     * 处理好友请求
     *
     * @param flag    好友请求flag
     * @param approve 是否同意好友请求
     * @param remark  好友备注
     */
    public static void handleFriendAddRequest(String flag, boolean approve, String remark) {
        JSONObject data = new JSONObject();
        data.put("flag", flag);
        data.put("approve", approve);
        data.put("remark", remark);

        getOneBotHttpClient().post("set_friend_add_request", data.toString());
    }

    /**
     * 同意进群请求
     *
     * @param flag 进群请求flag
     * @param type 进群请求类型
     */
    public static void handleGroupAddRequest(String flag, RequestSubType type) {
        handleGroupAddRequest(flag, type, true, null);
    }

    /**
     * 处理进群请求
     *
     * @param flag    进群请求flag
     * @param type    进群请求类型
     * @param approve 是否同意进群请求
     */
    public static void handleGroupAddRequest(String flag, RequestSubType type, boolean approve) {
        handleGroupAddRequest(flag, type, approve, null);
    }

    /**
     * 处理进群请求
     *
     * @param flag    进群请求flag
     * @param type    进群请求类型
     * @param approve 是否同意进群请求
     * @param reason  拒绝原因
     */
    public static void handleGroupAddRequest(String flag, RequestSubType type, boolean approve, String reason) {
        JSONObject data = new JSONObject();
        data.put("flag", flag);
        data.put("type", type.toString().toLowerCase(Locale.ROOT));
        data.put("approve", approve);
        data.put("reason", reason);

        getOneBotHttpClient().post("set_group_add_request", data.toString());
    }

    /**
     * 获取账户登录信息
     *
     * @return 账户登录信息实例
     */
    public static LoginInfo getLoginInfo() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_login_info"));

        return new LoginInfo(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 获取指定用户的陌生人信息实例
     *
     * @param userId 目标QQ号
     * @return 陌生人信息实例
     */
    public static Stranger getStrangerInfo(Long userId) {
        return getStrangerInfo(userId, true);
    }

    /**
     * 获取指定用户的陌生人信息实例
     *
     * @param userId 目标QQ号
     * @param cache  是否使用缓存
     * @return 陌生人信息实例
     */
    public static Stranger getStrangerInfo(Long userId, boolean cache) {
        JSONObject data = new JSONObject();
        data.put("user_id", userId);
        data.put("no_cache", !cache);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_stranger_info", data.toString()));

        return new Stranger(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 获取好友列表
     *
     * @return 好友实例列表
     */
    public static List<Friend> getFriendList() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_friend_list"));

        return Objects.requireNonNull(returnData).getList("data", Friend.class);
    }

    /**
     * 获取群聊列表
     *
     * @return 群聊实例列表
     */
    public static List<Group> getGroupList() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_list"));

        return Objects.requireNonNull(returnData).getList("data", Group.class);
    }

    /**
     * 获取指定群聊中指定用户的群成员实例
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @return 群成员实例
     */
    public static Member getGroupMemberInfo(Long groupId, Long userId) {
        return getGroupMemberInfo(groupId, userId, true);
    }

    /**
     * 获取指定群聊中指定用户的群成员实例
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @param cache   是否使用缓存
     * @return 群成员实例
     */
    public static Member getGroupMemberInfo(Long groupId, Long userId, boolean cache) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("user_id", userId);
        data.put("no_cache", !cache);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_member_info", data.toString()));

        return new Member(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 获取指定群聊中的群成员实例列表
     *
     * @param groupId 目标群号
     * @return 群成员实例列表
     */
    public static List<Member> getGroupMemberList(Long groupId) {
        return getGroupMemberList(groupId, true);
    }

    /**
     * 获取指定群聊中的群成员实例列表
     *
     * @param groupId 目标群号
     * @param cache   是否使用缓存
     * @return 群成员实例列表
     */
    public static List<Member> getGroupMemberList(Long groupId, boolean cache) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("no_cache", !cache);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_member_list", data.toString()));

        return Objects.requireNonNull(returnData).getList("data", Member.class);
    }

    /**
     * 获取指定群聊的群荣誉
     *
     * @param groupId 目标群号
     * @return 群荣誉实例
     */
    public static GroupHonor getGroupHonorInfo(Long groupId) {
        return getGroupHonorInfo(groupId, HonorType.ALL);
    }

    /**
     * 获取指定群聊的群荣誉
     *
     * @param groupId 目标群号
     * @param type    群荣誉类型
     * @return 群荣誉实例
     */
    public static GroupHonor getGroupHonorInfo(Long groupId, HonorType type) {
        JSONObject data = new JSONObject();
        data.put("group_id", groupId);
        data.put("type", type.toString().toLowerCase(Locale.ROOT));

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_group_honor_info", data.toString()));

        return new GroupHonor(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 获取指定地址的cookie
     *
     * @param domain 目标地址
     * @return cookie文本
     */
    public static String getCookies(String domain) {
        JSONObject data = new JSONObject();
        data.put("domain", domain);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_cookies", data.toString()));

        return Objects.requireNonNull(returnData).getString("cookies");
    }

    /**
     * 获取csrfToken
     *
     * @return csrfToken值
     */
    public static Long getCsrfToken() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_csrf_token"));

        return Objects.requireNonNull(returnData).getLong("token");
    }

    /**
     * 获取指定文件ID的语音实例
     *
     * @param file 语音文件ID
     * @return 语音实例
     */
    public static Record getRecord(String file) {
        return getRecord(file, RecordFormat.WAV);
    }

    /**
     * 获取指定文件ID的语音实例
     *
     * @param file   语音文件ID
     * @param format 语音导出格式
     * @return 语音实例
     */
    public static Record getRecord(String file, RecordFormat format) {
        JSONObject data = new JSONObject();
        data.put("file", file);
        data.put("format", format.toString());

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_record", data.toString()));

        return new Record(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 获取指定文件ID的图片文件路径
     *
     * @param file 图片文件ID
     * @return 图片文件路径
     */
    public static File getImage(String file) {
        JSONObject data = new JSONObject();
        data.put("file", file);

        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_image", data.toString()));

        return new java.io.File(Objects.requireNonNull(returnData).getJSONObject("data").getString("file"));
    }

    /**
     * 判断是否能发送语音
     *
     * @return 是或否
     */
    public static Boolean ifCanSendRecord() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("if_can_send_record"));

        return Objects.requireNonNull(returnData).getJSONObject("data").getBoolean("yes");
    }

    /**
     * 判断是否能发送图片
     *
     * @return 是或否
     */
    public static Boolean ifCanSendImage() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("if_can_send_image"));

        return Objects.requireNonNull(returnData).getJSONObject("data").getBoolean("yes");
    }

    /**
     * 获取运行状态实例
     *
     * @return 运行状态实例
     */
    public static Status getStatus() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_status"));

        return new Status(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 获取版本信息实例
     *
     * @return 版本信息实例
     */
    public static VersionInfo getVersionInfo() {
        JSONObject returnData = JSONObject.parseObject(getOneBotHttpClient().post("get_version_info"));

        return new VersionInfo(Objects.requireNonNull(returnData).getJSONObject("data"));
    }

    /**
     * 重启oneBot实现
     */
    public static void restart() {
        restart(0L);
    }

    /**
     * 重启oneBot实现
     *
     * @param delay 延迟(单位: 毫秒)
     */
    public static void restart(Long delay) {
        JSONObject data = new JSONObject();
        data.put("delay", delay);

        getOneBotHttpClient().post("set_restart", data.toString());
    }

    /**
     * 清理缓存1
     */
    public static void cleanCache() {
        getOneBotHttpClient().post("clean_cache");
    }
}
