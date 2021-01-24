package net.craftgalaxy.asyncteleport.listener;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class BukkitListener<T extends Event> implements Listener {

    protected AsyncTeleport plugin;

    public BukkitListener(AsyncTeleport plugin) {
        this.plugin = plugin;
    }

    public void disable() {
        HandlerList.unregisterAll(this);
        this.plugin = null;
    }

    public abstract void handleEvent(T e);
}
