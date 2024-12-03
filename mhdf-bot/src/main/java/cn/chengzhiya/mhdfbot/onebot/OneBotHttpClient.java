package cn.chengzhiya.mhdfbot.onebot;

import cn.chengzhiya.mhdfbot.Main;
import cn.chengzhiya.mhdfbot.api.MHDFBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("CallToPrintStackTrace")
public final class OneBotHttpClient {
    private final String httpHost = Main.getConfigManager().getConfig().getString("oneBotSettings.httpHost");
    private final String accessToken = Main.getConfigManager().getConfig().getString("oneBotSettings.accessToken");

    /**
     * 获取连接实例
     *
     * @param urlString 请求地址
     * @return 连接地址
     */
    private HttpURLConnection getConnection(String urlString) {
        try {
            URL url = new URL(httpHost + urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(7500);
            connection.setReadTimeout(7500);

            if (accessToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定连接实例的数据
     *
     * @param connection 连接实例
     * @return 数据
     */
    private String getData(HttpURLConnection connection) {
        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    return in.readLine();
                }
            } else {
                MHDFBot.getLogger().info(
                        "向{}发送{}请求失败 #{}({})",
                        connection.getURL(),
                        connection.getRequestMethod(),
                        connection.getResponseCode(),
                        connection.getResponseMessage()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return null;
    }

    /**
     * 发送GET请求获取数据
     *
     * @param urlString 请求地址
     * @return 数据
     */
    public String get(String urlString) {
        try {
            HttpURLConnection connection = getConnection(urlString);
            Objects.requireNonNull(connection).setRequestMethod("GET");

            return getData(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送POST请求获取数据
     *
     * @param urlString 请求地址
     * @return 数据
     */
    public String post(String urlString) {
        try {
            HttpURLConnection connection = getConnection(urlString);
            Objects.requireNonNull(connection).setRequestMethod("POST");

            return getData(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送POST请求获取数据
     *
     * @param urlString 请求地址
     * @param data      请求头数据
     * @return 数据
     */
    public String post(String urlString, String data) {
        try {
            HttpURLConnection connection = getConnection(urlString);
            Objects.requireNonNull(connection).setRequestMethod("POST");

            connection.setDoOutput(true);
            try (OutputStream out = connection.getOutputStream()) {
                out.write(data.getBytes());
                out.flush();
            }

            return getData(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
