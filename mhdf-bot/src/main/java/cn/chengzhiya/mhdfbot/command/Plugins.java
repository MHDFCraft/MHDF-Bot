package cn.chengzhiya.mhdfbot.command;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.command.CommandExecutor;
import cn.chengzhiya.mhdfbot.api.entity.plugin.PluginInfo;
import cn.chengzhiya.mhdfbot.api.enums.plugin.PluginStatus;

import java.util.List;

public final class Plugins implements CommandExecutor {
    @Override
    public void onCommand(String command, String[] args) {
        List<PluginInfo> pluginInfoList = MHDFBot.getPluginManager().getPluginList();

        // 输出已加载插件实例列表
        this.printPluginList(pluginInfoList, "已加载插件", PluginStatus.Load_Done);

        // 输出未加载插件实例列表
        this.printPluginList(pluginInfoList, "未加载插件", PluginStatus.Load_Error);
    }

    /**
     * 输出插件实例列表
     *
     * @param pluginInfoList 插件实例列表
     * @param prefix         前缀
     * @param pluginStatus   插件状态
     */
    private void printPluginList(List<PluginInfo> pluginInfoList, String prefix, PluginStatus pluginStatus) {
        List<PluginInfo> filterPluginInfoList = pluginInfoList.stream()
                .filter(pluginInfo -> pluginInfo.getPluginStatus() == pluginStatus)
                .toList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix).append("(").append(filterPluginInfoList.size()).append(")").append(": ");
        filterPluginInfoList.forEach(pluginInfo -> stringBuilder.append(pluginInfo.getName()).append(", "));

        MHDFBot.getLogger().info(stringBuilder.toString());
    }
}
