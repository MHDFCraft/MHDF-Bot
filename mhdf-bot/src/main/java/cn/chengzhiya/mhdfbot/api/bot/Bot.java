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

import java.io.File;
import java.util.List;

public interface Bot {
    /**
     * 清理缓存
     */
    void cleanCache();

    /**
     * 重启oneBot实现
     *
     * @param delay 延迟(单位: 毫秒)
     */
    void restart(Long delay);

    /**
     * 重启oneBot实现
     */
    void restart();

    /**
     * 获取运行状态实例
     *
     * @return 运行状态实例
     */
    Status getStatus();

    /**
     * 获取版本信息实例
     *
     * @return 版本信息实例
     */
    VersionInfo getVersionInfo();

    /**
     * 获取账户登录信息
     *
     * @return 账户登录信息实例
     */
    LoginInfo getLoginInfo();

    /**
     * 判断是否能发送语音
     *
     * @return 是或否
     */
    Boolean ifCanSendRecord();

    /**
     * 判断是否能发送图片
     *
     * @return 是或否
     */
    Boolean ifCanSendImage();

    /**
     * 获取csrfToken
     *
     * @return csrfToken值
     */
    Long getCsrfToken();

    /**
     * 获取好友列表
     *
     * @return 好友实例列表
     */
    List<Friend> getFriendList();

    /**
     * 获取群聊列表
     *
     * @return 群聊实例列表
     */
    List<Group> getGroupList();

    /**
     * 通过消息ID获取消息事件
     *
     * @param messageId 消息ID
     * @return 消息事件
     */
    AbstractMessageEvent getMsg(Long messageId);

    /**
     * 发送消息
     *
     * @param messageType 目标类型
     * @param targetId    目标QQ号或目标群号
     * @param message     消息内容
     * @param autoEscape  消息内容是否作为纯文本发送
     * @return 消息ID
     */
    Long sendMsg(MessageType messageType, Long targetId, String message, boolean autoEscape);

    /**
     * 发送私聊消息
     *
     * @param targetId   目标QQ号
     * @param message    消息内容
     * @param autoEscape 消息内容是否作为纯文本发送
     * @return 消息ID
     */
    Long sendPrivateMsg(Long targetId, String message, boolean autoEscape);

    /**
     * 发送私聊消息
     *
     * @param targetId 目标QQ号
     * @param message  消息内容
     * @return 消息ID
     */
    Long sendPrivateMsg(Long targetId, String message);

    /**
     * 发送私聊消息
     *
     * @param targetId   目标群号
     * @param message    消息内容
     * @param autoEscape 消息内容是否作为纯文本发送
     * @return 消息ID
     */
    Long sendGroupMsg(Long targetId, String message, boolean autoEscape);

    /**
     * 发送私聊消息
     *
     * @param targetId 目标群号
     * @param message  消息内容
     * @return 消息ID
     */
    Long sendGroupMsg(Long targetId, String message);

    /**
     * 通过消息ID撤回消息
     *
     * @param messageId 消息ID
     */
    void deleteMsg(Long messageId);

    /**
     * 给好友点赞
     *
     * @param targetId 目标QQ号
     * @param times    点赞次数
     */
    void sendLike(Long targetId, int times);

    /**
     * 踢出指定群聊中的指定群员
     *
     * @param groupId          目标群号
     * @param userId           目标QQ号
     * @param rejectAddRequest 拒绝此人再次加群请求
     */
    void groupKick(Long groupId, Long userId, boolean rejectAddRequest);

    /**
     * 踢出指定群聊中的指定群员
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void groupKick(Long groupId, Long userId);

    /**
     * 禁言指定群聊中的指定群员
     *
     * @param groupId  目标群号
     * @param userId   目标QQ号
     * @param duration 禁言时长(单位:秒) 0秒为取消禁言
     */
    void setGroupMute(Long groupId, Long userId, Long duration);

    /**
     * 禁言指定群聊中的指定群员30分钟
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void setGroupMute(Long groupId, Long userId);

    /**
     * 解除禁言指定群聊中的指定群员
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void unsetGroupMute(Long groupId, Long userId);

    /**
     * 设置指定群聊全体禁言
     *
     * @param groupId 目标群号
     * @param enable  是否禁言
     */
    void setGroupWholeMute(Long groupId, boolean enable);

    /**
     * 开启指定群聊全体禁言
     *
     * @param groupId 目标群号
     */
    void setGroupWholeMute(Long groupId);

    /**
     * 关闭指定群聊全体禁言
     *
     * @param groupId 目标群号
     */
    void unsetGroupWholeMute(Long groupId);

    /**
     * 设置指定群聊指定用户的群管理
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @param enable  是否设置
     */
    void setGroupAdmin(Long groupId, Long userId, boolean enable);

    /**
     * 设置指定群聊指定用户为群管理
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void setGroupAdmin(Long groupId, Long userId);

    /**
     * 取消指定群聊指定用户的群管理
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void unsetGroupAdmin(Long groupId, Long userId);

    /**
     * 设置指定群聊指定用户的群昵称
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @param card    群昵称文本(为空删除群昵称)
     */
    void setGroupCard(Long groupId, Long userId, String card);

    /**
     * 删除指定群聊指定用户的群昵称
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void unsetGroupCard(Long groupId, Long userId);

    /**
     * 设置指定群聊的群名称
     *
     * @param groupId 目标群号
     * @param name    群名称文本
     */
    void setGroupName(Long groupId, String name);

    /**
     * 退出指定群聊
     *
     * @param groupId 目标群号
     * @param dismiss 是否解散
     */
    void leaveGroup(Long groupId, boolean dismiss);

    /**
     * 退出指定群聊
     *
     * @param groupId 目标群号
     */
    void leaveGroup(Long groupId);

    /**
     * 解散指定群聊
     *
     * @param groupId 目标群号
     */
    void dismissGroup(Long groupId);

    /**
     * 设置指定群聊指定用户的头衔
     *
     * @param groupId      目标群号
     * @param userId       目标QQ号
     * @param specialTitle 头衔文本(为空删除头衔)
     * @param duration     头衔有效期
     */
    void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle, Long duration);

    /**
     * 设置指定群聊指定用户的头衔
     *
     * @param groupId      目标群号
     * @param userId       目标QQ号
     * @param specialTitle 头衔文本(为空删除头衔)
     */
    void setGroupSpecialTitle(Long groupId, Long userId, String specialTitle);

    /**
     * 删除指定群聊指定用户的头衔
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     */
    void unsetGroupSpecialTitle(Long groupId, Long userId);

    /**
     * 处理好友请求
     *
     * @param flag    好友请求flag
     * @param approve 是否同意好友请求
     * @param remark  好友备注
     */
    void handleFriendAddRequest(String flag, boolean approve, String remark);

    /**
     * 处理好友请求
     *
     * @param flag    好友请求flag
     * @param approve 是否同意好友请求
     */
    void handleFriendAddRequest(String flag, boolean approve);

    /**
     * 同意好友请求
     *
     * @param flag 好友请求flag
     */
    void acceptFriendAddRequest(String flag);

    /**
     * 拒绝好友请求
     *
     * @param flag 好友请求flag
     */
    void rejectFriendAddRequest(String flag);

    /**
     * 处理进群请求
     *
     * @param flag    进群请求flag
     * @param type    进群请求类型
     * @param approve 是否同意进群请求
     * @param reason  拒绝原因
     */
    void handleGroupAddRequest(String flag, RequestSubType type, boolean approve, String reason);

    /**
     * 处理进群请求
     *
     * @param flag    进群请求flag
     * @param type    进群请求类型
     * @param approve 是否同意进群请求
     */
    void handleGroupAddRequest(String flag, RequestSubType type, boolean approve);

    /**
     * 同意进群请求
     *
     * @param flag 进群请求flag
     * @param type 进群请求类型
     */
    void acceptGroupAddRequest(String flag, RequestSubType type);

    /**
     * 拒绝进群请求
     *
     * @param flag 进群请求flag
     * @param type 进群请求类型
     */
    void rejectGroupAddRequest(String flag, RequestSubType type);

    /**
     * 获取指定用户的陌生人信息实例
     *
     * @param userId 目标QQ号
     * @param cache  是否使用缓存
     * @return 陌生人信息实例
     */
    Stranger getStrangerInfo(Long userId, boolean cache);

    /**
     * 获取指定用户的陌生人信息实例
     *
     * @param userId 目标QQ号
     * @return 陌生人信息实例
     */
    Stranger getStrangerInfo(Long userId);

    /**
     * 获取指定群聊中指定用户的群成员实例
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @param cache   是否使用缓存
     * @return 群成员实例
     */
    Member getGroupMemberInfo(Long groupId, Long userId, boolean cache);

    /**
     * 获取指定群聊中指定用户的群成员实例
     *
     * @param groupId 目标群号
     * @param userId  目标QQ号
     * @return 群成员实例
     */
    Member getGroupMemberInfo(Long groupId, Long userId);

    /**
     * 获取指定群聊中的群成员实例列表
     *
     * @param groupId 目标群号
     * @param cache   是否使用缓存
     * @return 群成员实例列表
     */
    List<Member> getGroupMemberList(Long groupId, boolean cache);

    /**
     * 获取指定群聊中的群成员实例列表
     *
     * @param groupId 目标群号
     * @return 群成员实例列表
     */
    List<Member> getGroupMemberList(Long groupId);

    /**
     * 获取指定群聊的群荣誉
     *
     * @param groupId 目标群号
     * @param type    群荣誉类型
     * @return 群荣誉实例
     */
    GroupHonor getGroupHonorInfo(Long groupId, HonorType type);

    /**
     * 获取指定群聊的群荣誉
     *
     * @param groupId 目标群号
     * @return 群荣誉实例
     */
    GroupHonor getGroupHonorInfo(Long groupId);

    /**
     * 获取指定地址的cookie
     *
     * @param domain 目标地址
     * @return cookie文本
     */
    String getCookies(String domain);

    /**
     * 获取指定文件ID的语音实例
     *
     * @param file   语音文件ID
     * @param format 语音导出格式
     * @return 语音实例
     */
    Record getRecord(String file, RecordFormat format);

    /**
     * 获取指定文件ID的语音实例
     *
     * @param file 语音文件ID
     * @return 语音实例
     */
    Record getRecord(String file);

    /**
     * 获取指定文件ID的图片文件路径
     *
     * @param file 图片文件ID
     * @return 图片文件路径
     */
    File getImage(String file);
}
