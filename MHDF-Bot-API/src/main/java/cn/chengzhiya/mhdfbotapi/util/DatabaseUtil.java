package cn.chengzhiya.mhdfbotapi.util;

import cn.chengzhiya.mhdfbotapi.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotapi.entity.Marry;
import cn.chengzhiya.mhdfbotapi.entity.PlayerData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public final class DatabaseUtil {
    @Getter
    public static final HashMap<String, PlayerData> playerDataHashMap = new HashMap<>();
    @Getter
    public static final HashMap<String, Long> playerQQHashMap = new HashMap<>();
    @Getter
    public static final HashMap<Long, List<String>> qqBindListHashMap = new HashMap<>();
    public static Statement statement;
    public static HikariDataSource dataSource;
    public static Statement loginSystemStatement;
    public static HikariDataSource loginSystemDataSource;

    public static HikariDataSource getDataSource(DatabaseConfig database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + database.getHost() + "/" + database.getDatabase() + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
        config.setUsername(database.getUsername());
        config.setPassword(database.getPassword());
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }

    public static void intiDatabase(DatabaseConfig botDatabase, DatabaseConfig loginDatabase) {
        try {
            {
                dataSource = getDataSource(botDatabase);
                statement = dataSource.getConnection().createStatement();
            }

            {
                if (loginDatabase != null) {
                    loginSystemDataSource = getDataSource(loginDatabase);
                    loginSystemStatement = loginSystemDataSource.getConnection().createStatement();
                }
            }

            {
                try (Connection connection = dataSource.getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS `mhdf-bot`.mhdfbot_bindqq" +
                                    "(" +
                                    "    PlayerName VARCHAR(50) NOT NULL COMMENT '玩家ID'," +
                                    "    QQ BIGINT DEFAULT 0 NOT NULL COMMENT 'QQ号'," +
                                    "    ChatTimes INT DEFAULT 0 NOT NULL COMMENT '积累聊天数'," +
                                    "    DayChatTimes INT DEFAULT 0 NOT NULL COMMENT '今日聊天数'," +
                                    "    PRIMARY KEY (PlayerName)," +
                                    "    INDEX `QQ` (`QQ`)" +
                                    ")" +
                                    "COLLATE=utf8mb4_bin;"
                    )) {
                        ps.executeUpdate();
                    }
                }
            }

            {
                try (Connection connection = dataSource.getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement(
                            "CREATE TABLE IF NOT EXISTS mhdfbot_marry" +
                                    "(" +
                                    "    MrQQ BIGINT DEFAULT 0 NOT NULL COMMENT '老公QQ'," +
                                    "    MrsQQ BIGINT DEFAULT 0 NOT NULL COMMENT '老婆QQ'," +
                                    "    PRIMARY KEY (MrQQ)," +
                                    "    INDEX `MrsQQ` (`MrsQQ`)" +
                                    ")" +
                                    "COLLATE=utf8mb4_bin;"
                    )) {
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ifPlayerDataExist(String playerName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_bindqq where PlayerName=?;")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ifPlayerDataExist(Long QQ) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_bindqq where QQ=?;")) {
                ps.setLong(1, QQ);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerData getPlayerData(String playerName) {
        if (getPlayerDataHashMap().get(playerName) == null) {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_bindqq where PlayerName=?;")) {
                    ps.setString(1, playerName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            PlayerData playerData = new PlayerData(playerName, rs.getLong("QQ"), rs.getLong("ChatTimes"), rs.getInt("DayChatTimes"));
                            getPlayerQQHashMap().put(playerName, rs.getLong("QQ"));
                            getPlayerDataHashMap().put(playerName, playerData);
                            return playerData;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getPlayerDataHashMap().get(playerName);
        }
        return null;
    }

    public static void updatePlayerData(String playerName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_bindqq where PlayerName=?;")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        getPlayerDataHashMap().put(playerName, new PlayerData(playerName, rs.getLong("QQ"), rs.getLong("ChatTimes"), rs.getInt("DayChatTimes")));
                        getPlayerQQHashMap().put(playerName, rs.getLong("QQ"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bind(PlayerData playerData) {
        getPlayerQQHashMap().put(playerData.getPlayerName(), playerData.getQQ());
        getPlayerDataHashMap().put(playerData.getPlayerName(), playerData);

        List<String> bindList = new ArrayList<>(getQqBindListHashMap().get(playerData.getQQ()));
        bindList.add(playerData.getPlayerName());

        getQqBindListHashMap().put(playerData.getQQ(), bindList);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("insert into `mhdf-bot`.mhdfbot_bindqq (PlayerName, QQ) values (?,?);")) {
                ps.setString(1, playerData.getPlayerName());
                ps.setLong(2, playerData.getQQ());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unbind(PlayerData playerData) {
        getPlayerQQHashMap().put(playerData.getPlayerName(), null);
        getPlayerDataHashMap().put(playerData.getPlayerName(), null);

        List<String> bindList = new ArrayList<>();
        if (getQqBindListHashMap().get(playerData.getQQ()) != null) {
            bindList.addAll(getQqBindListHashMap().get(playerData.getQQ()));
        }
        bindList.remove(playerData.getPlayerName());

        getQqBindListHashMap().put(playerData.getQQ(), bindList);
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("delete from `mhdf-bot`.mhdfbot_bindqq where PlayerName=? AND QQ=?;")) {
                ps.setString(1, playerData.getPlayerName());
                ps.setLong(2, playerData.getQQ());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PlayerData> getPlayerDataList(Long QQ) {
        List<PlayerData> playerDataList = new ArrayList<>();
        if (getQqBindListHashMap().get(QQ) == null) {
            List<String> bindList = new ArrayList<>();
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_bindqq where QQ=?;")) {
                    ps.setLong(1, QQ);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String playerName = rs.getString("PlayerName");
                            bindList.add(playerName);
                            playerDataList.add(new PlayerData(playerName, QQ));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            getQqBindListHashMap().put(QQ, bindList);
        } else {
            for (String playerName : getQqBindListHashMap().get(QQ)) {
                playerDataList.add(new PlayerData(playerName, QQ));
            }
        }
        return playerDataList;
    }

    public static void changePassword(String PlayerName, String NewPasswordSha256) {
        try {
            Connection connection = loginSystemDataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("update mhdflogin.LoginData set Password = ? where PlayerName = ?");
            ps.setString(1, NewPasswordSha256);
            ps.setString(2, PlayerName);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ifMarryDataExist(Long QQ) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_marry where MrQQ=? or MrsQQ=?;")) {
                ps.setLong(1, QQ);
                ps.setLong(2, QQ);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isMr(Long QQ) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_marry where MrQQ=?;")) {
                ps.setLong(1, QQ);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Long> getMarryList() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_marry")) {
                try (ResultSet rs = ps.executeQuery()) {
                    List<Long> MarryList = new ArrayList<>();
                    while (rs.next()) {
                        MarryList.add(rs.getLong("MrQQ"));
                        MarryList.add(rs.getLong("MrsQQ"));
                    }
                    return MarryList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void marry(Marry marry) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("insert into `mhdf-bot`.mhdfbot_marry (MrQQ, MrsQQ) values (?,?);")) {
                ps.setLong(1, marry.getMrQQ());
                ps.setLong(2, marry.getMrsQQ());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Marry getMarryData(Long QQ) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from `mhdf-bot`.mhdfbot_marry where MrQQ=? or MrsQQ=?;")) {
                ps.setLong(1, QQ);
                ps.setLong(2, QQ);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Marry(rs.getLong("MrQQ"), rs.getLong("MrsQQ"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void clearMarry() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("delete from `mhdf-bot`.mhdfbot_marry;")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addChatTimes(Long QQ, int Times) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("update `mhdf-bot`.mhdfbot_bindqq set DayChatTimes=DayChatTimes+?,ChatTimes=ChatTimes+? where QQ=?;")) {
                ps.setLong(1, Times);
                ps.setLong(2, Times);
                ps.setLong(3, QQ);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearDayChatTimes() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("update `mhdf-bot`.mhdfbot_bindqq set DayChatTimes = 0;")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
