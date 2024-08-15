package cn.ChengZhiYa.MHDFBot.util;

import cn.ChengZhiYa.MHDFBot.entity.YamlConfiguration;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.ignore.SensitiveWordCharIgnores;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.action.response.GroupMemberInfoResp;
import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public final class Util {
    @Getter
    public static final HashMap<Long, String> ChangePasswordHashMap = new HashMap<>();
    @Getter
    public static final HashMap<String, Integer> VerifyCodeHashMap = new HashMap<>();
    public static final SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
            .ignoreCase(true)
            .ignoreWidth(true)
            .ignoreNumStyle(true)
            .ignoreChineseStyle(true)
            .ignoreEnglishStyle(true)
            .ignoreRepeat(true)
            .enableNumCheck(false)
            .enableEmailCheck(false)
            .enableUrlCheck(false)
            .enableWordCheck(true)
            .numCheckLen(999)
            .charIgnore(SensitiveWordCharIgnores.specialChars())
            .wordResultCondition(WordResultConditions.alwaysTrue())
            .init();
    public static YamlConfiguration Lang;
    @Getter
    public static YamlConfiguration Config;

    public static String i18n(String Path) {
        if (Lang == null) {
            Util.reloadLang();
        }
        return Lang.getString(Path);
    }

    public static String sha256(String message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuffer = new StringBuilder();
            for (byte aByte : messageDigest.digest()) {
                String temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(temp);
            }
            return stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void reloadLang() {
        Lang = YamlConfiguration.loadConfiguration(new File("lang.yml"));
    }

    public static void reloadConfig() {
        Config = YamlConfiguration.loadConfiguration(new File("config.yml"));
    }

    public static InputStream getResource(String filename) {
        try {
            URL URL2 = Util.class.getClassLoader().getResource(filename);
            if (URL2 == null) {
                return null;
            }
            URLConnection Connection = URL2.openConnection();
            Connection.setUseCaches(false);
            return Connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    public static void saveResource(String FilePath, String OutFileName, String ResourcePath, boolean Replace) {
        if (ResourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        InputStream in = Util.getResource(ResourcePath = ResourcePath.replace('\\', '/'));
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + ResourcePath + "' cannot be found in " + ResourcePath);
        }
        File OutFile = new File(FilePath, OutFileName);
        try {
            if (!OutFile.exists() || Replace) {
                int Len;
                FileOutputStream out = new FileOutputStream(OutFile);
                byte[] buf = new byte[1024];
                while ((Len = in.read(buf)) > 0) {
                    out.write(buf, 0, Len);
                }
                out.close();
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createFile(String FilePath, String FileName, boolean Replace) {
        if (FilePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
        File OutFile = new File(FilePath, FileName);
        try {
            if (OutFile.exists() && Replace) {
                OutFile.delete();
            }
            if (!OutFile.exists()) {
                OutFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Long> getMemberList(Bot bot, Long group) {
        List<Long> memberList = new ArrayList<>();
        for (GroupMemberInfoResp member : bot.getGroupMemberList(group).getData()) {
            memberList.add(member.getUserId());
        }
        return memberList;
    }

    public static boolean ifContainsBlackWord(String Message) {
        if (Util.getConfig().getBoolean("BlackWordSettings.Enable")) {
            if (Message.startsWith("[CQ:image") && Message.contains("url=https://multimedia.nt.qq.com.cn/download")) {
                return false;
            }
            if (Util.getConfig().getBoolean("BlackWordSettings.EnableSensitiveWordAPI")) {
                if (wordBs.contains(Message)) {
                    return true;
                }
            }
            for (String BlackWords : Util.getConfig().getStringList("BlackWordSettings.BlackWordList")) {
                if (Message.toLowerCase(Locale.ROOT).contains(BlackWords.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getVerifyCode(String playerName) {
        if (getVerifyCodeHashMap().get(playerName) == null) {
            Random random = new Random();
            getVerifyCodeHashMap().put(playerName, random.nextInt(999999));
        }
        return getVerifyCodeHashMap().get(playerName);
    }

    public static void removeVerifyCode(String playerName) {
        if (getVerifyCodeHashMap().get(playerName) != null) {
            getVerifyCodeHashMap().remove(playerName);
        }
    }

    public static void sendMessageToAllowUseBotGroups(Bot bot, String Message) {
        for (String groupID : Util.getConfig().getStringList("AllowUseBotList")) {
            bot.sendGroupMsg(Long.parseLong(groupID), Message, false);
        }
    }
}

