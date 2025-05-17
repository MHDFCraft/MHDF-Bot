package cn.chengzhiya.mhdfbot.api;

import cn.chengzhiya.mhdfbot.api.bot.Bot;
import cn.chengzhiya.mhdfbot.api.bot.BotOneBotImpl;
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
import cn.chengzhiya.mhdfbot.api.manager.CommandManager;
import cn.chengzhiya.mhdfbot.api.manager.ListenerManager;
import cn.chengzhiya.mhdfbot.api.manager.PluginManager;
import cn.chengzhiya.mhdfbot.api.manager.SchedulerManager;
import cn.chengzhiya.mhdfbot.minecraft.MinecraftWebSocketServer;
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

@SuppressWarnings("unused")
public final class MHDFBot implements Bot {
    @Getter
    private static final Logger logger = getLogger("MHDF-Bot");
    @Getter
    private static final Bot bot;
    @Getter
    private static final PluginManager pluginManager = new PluginManager();
    @Getter
    private static final CommandManager commandManager = new CommandManager();
    @Getter
    private static final ListenerManager listenerManager = new ListenerManager();
    @Getter
    private static final MinecraftWebSocketServer minecraftWebSocketServer = new MinecraftWebSocketServer();

    static {
        bot = new BotOneBotImpl();
    }

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

    @Override
    public void cleanCache() {
        getBot().cleanCache();
    }

    @Override
    public void restart(Long delay) {
        getBot().restart(delay);
    }

    @Override
    public void restart() {
        getBot().restart();
    }

    @Override
    public Status getStatus() {
        return getBot().getStatus();
    }

    @Override
    public VersionInfo getVersionInfo() {
        return getBot().getVersionInfo();
    }

    @Override
    public LoginInfo getLoginInfo() {
        return getBot().getLoginInfo();
    }

    @Override
    public Boolean ifCanSendRecord() {
        return getBot().ifCanSendRecord();
    }

    @Override
    public Boolean ifCanSendImage() {
        return getBot().ifCanSendImage();
    }

    @Override
    public Long getCsrfToken() {
        return getBot().getCsrfToken();
    }

    @Override
    public List<Friend> getFriendList() {
        return getBot().getFriendList();
    }

    @Override
    public List<Group> getGroupList() {
        return getBot().getGroupList();
    }

    @Override
    public AbstractMessageEvent getMsg(Long messageId) {
        return getBot().getMsg(messageId);
    }

    @Override
    public Long sendMsg(MessageType messageType, Long targetId, String message, boolean autoEscape) {
        return getBot().sendMsg(messageType, targetId, message, autoEscape);
    }

    @Override
    public Long sendPrivateMsg(Long targetId, String message, boolean autoEscape) {
        return getBot().sendPrivateMsg(targetId, message, autoEscape);
    }

    @Override
    public Long sendPrivateMsg(Long targetId, String message) {
        return getBot().sendPrivateMsg(targetId, message);
    }

    @Override
    public Long sendGroupMsg(Long targetId, String message, boolean autoEscape) {
        return getBot().sendGroupMsg(targetId, message, autoEscape);
    }

    @Override
    public Long sendGroupMsg(Long targetId, String message) {
        return getBot().sendGroupMsg(targetId, message);
    }

    @Override
    public void deleteMsg(Long messageId) {
        getBot().deleteMsg(messageId);
    }

    @Override
    public void sendLike(Long targetId, int times) {
        getBot().sendLike(targetId, times);
    }

    @Override
    public void groupKick(Long groupId, Long userId, boolean rejectAddRequest) {
        getBot().groupKick(groupId, userId, rejectAddRequest);
    }

    @Override
    public void groupKick(Long groupId, Long userId) {
        getBot().groupKick(groupId, userId);
    }

    @Override
    public void setGroupMute(Long groupId, Long userId, Long duration) {
        getBot().setGroupMute(groupId, userId, duration);
    }

    @Override
    public void setGroupMute(Long groupId, Long userId) {
        getBot().setGroupMute(groupId, userId);
    }

    @Override
    public void unsetGroupMute(Long groupId, Long userId) {
        getBot().unsetGroupMute(groupId, userId);
    }

    @Override
    public void setGroupWholeMute(Long groupId, boolean enable) {
        getBot().setGroupWholeMute(groupId, enable);
    }

    @Override
    public void setGroupWholeMute(Long groupId) {
        getBot().setGroupWholeMute(groupId);
    }

    @Override
    public void unsetGroupWholeMute(Long groupId) {
        getBot().unsetGroupWholeMute(groupId);
    }

    @Override
    public void setGroupAdmin(Long groupId, Long userId, boolean enable) {
        getBot().setGroupAdmin(groupId, userId, enable);
    }

    @Override
    public void setGroupAdmin(Long groupId, Long userId) {
        getBot().setGroupAdmin(groupId, userId);
    }

    @Override
    public void unsetGroupAdmin(Long groupId, Long userId) {
        getBot().unsetGroupAdmin(groupId, userId);
    }

    @Override
    public void setGroupCard(Long groupId, Long userId, String card) {
        getBot().setGroupCard(groupId, userId, card);
    }

    @Override
    public void unsetGroupCard(Long groupId, Long userId) {
        getBot().unsetGroupCard(groupId, userId);
    }

    @Override
    public void setGroupName(Long groupId, String name) {
        getBot().setGroupName(groupId, name);
    }

    @Override
    public void leaveGroup(Long groupId, boolean dismiss) {
        getBot().leaveGroup(groupId, dismiss);
    }

    @Override
    public void leaveGroup(Long groupId) {
        getBot().leaveGroup(groupId);
    }

    @Override
    public void dismissGroup(Long groupId) {
        getBot().dismissGroup(groupId);
    }

    @Override
    public void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle, Long duration) {
        getBot().setGroupSpecialTitle(groupId, userId, specialTitle, duration);
    }

    @Override
    public void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
        getBot().setGroupSpecialTitle(groupId, userId, specialTitle);
    }

    @Override
    public void unsetGroupSpecialTitle(Long groupId, Long userId) {
        getBot().unsetGroupSpecialTitle(groupId, userId);
    }

    @Override
    public void handleFriendAddRequest(String flag, boolean approve, String remark) {
        getBot().handleFriendAddRequest(flag, approve, remark);
    }

    @Override
    public void handleFriendAddRequest(String flag, boolean approve) {
        getBot().handleFriendAddRequest(flag, approve);
    }

    @Override
    public void acceptFriendAddRequest(String flag) {
        getBot().acceptFriendAddRequest(flag);
    }

    @Override
    public void rejectFriendAddRequest(String flag) {
        getBot().rejectFriendAddRequest(flag);
    }

    @Override
    public void handleGroupAddRequest(String flag, RequestSubType type, boolean approve, String reason) {
        getBot().handleGroupAddRequest(flag, type, approve, reason);
    }

    @Override
    public void handleGroupAddRequest(String flag, RequestSubType type, boolean approve) {
        getBot().handleGroupAddRequest(flag, type, approve);
    }

    @Override
    public void acceptGroupAddRequest(String flag, RequestSubType type) {
        getBot().acceptGroupAddRequest(flag, type);
    }

    @Override
    public void rejectGroupAddRequest(String flag, RequestSubType type) {
        getBot().rejectGroupAddRequest(flag, type);
    }

    @Override
    public Stranger getStrangerInfo(Long userId, boolean cache) {
        return getBot().getStrangerInfo(userId, cache);
    }

    @Override
    public Stranger getStrangerInfo(Long userId) {
        return getBot().getStrangerInfo(userId);
    }

    @Override
    public Member getGroupMemberInfo(Long groupId, Long userId, boolean cache) {
        return getBot().getGroupMemberInfo(groupId, userId, cache);
    }

    @Override
    public Member getGroupMemberInfo(Long groupId, Long userId) {
        return getBot().getGroupMemberInfo(groupId, userId);
    }

    @Override
    public List<Member> getGroupMemberList(Long groupId, boolean cache) {
        return getBot().getGroupMemberList(groupId, cache);
    }

    @Override
    public List<Member> getGroupMemberList(Long groupId) {
        return getBot().getGroupMemberList(groupId);
    }

    @Override
    public GroupHonor getGroupHonorInfo(Long groupId, HonorType type) {
        return getBot().getGroupHonorInfo(groupId, type);
    }

    @Override
    public GroupHonor getGroupHonorInfo(Long groupId) {
        return getBot().getGroupHonorInfo(groupId);
    }

    @Override
    public String getCookies(String domain) {
        return getBot().getCookies(domain);
    }

    @Override
    public Record getRecord(String file, RecordFormat format) {
        return getBot().getRecord(file, format);
    }

    @Override
    public Record getRecord(String file) {
        return getBot().getRecord(file);
    }

    @Override
    public File getImage(String file) {
        return getBot().getImage(file);
    }
}
