package cn.chengzhiya.mhdfbotbukkithook.event;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class WebSocketEvent extends Event {
    private final HandlerList handlers = new HandlerList();
    private final JSONObject data;

    public WebSocketEvent(JSONObject data) {
        super(true);
        this.data = data;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return this.handlers;
    }
}
