package cn.ChengZhiYa.MHDFBot;

import cn.ChengZhiYa.MHDFBot.entity.plugin.PluginInfo;
import cn.ChengZhiYa.MHDFBot.server.WebSocket;
import cn.ChengZhiYa.MHDFBot.util.CommandUtil;
import lombok.Getter;

import java.util.List;
import java.util.Scanner;

import static cn.ChengZhiYa.MHDFBot.client.WebSocket.connectOneBotServer;
import static cn.ChengZhiYa.MHDFBot.command.CommandRegister.registerCommands;
import static cn.ChengZhiYa.MHDFBot.listener.ListenerRegister.registerListeners;
import static cn.ChengZhiYa.MHDFBot.util.ConfigUtil.saveDefaultConfig;
import static cn.ChengZhiYa.MHDFBot.util.PluginUtil.loadPlugins;

public class MHDFBot {
    @Getter
    private static final PluginInfo botInfo = new PluginInfo("MHDF-Bot", "1.0.0", List.of("ChengZhiYa"), "cn.ChengZhiYa.MHDFBot");

    public static void main(String[] args) throws Exception {
        saveDefaultConfig();

        cn.ChengZhiYa.MHDFBot.api.MHDFBot.getScheduler().runTask(WebSocket::startWebSocketServer);
        connectOneBotServer();

        registerCommands();
        registerListeners();
        loadPlugins();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            CommandUtil.executeCommand(scanner.nextLine());
        }
    }
}
