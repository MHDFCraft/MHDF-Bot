package cn.chengzhiya.mhdfbot.api.command;

@SuppressWarnings("unused")
public interface CommandExecutor {
    void onCommand(String command, String[] args);
}
