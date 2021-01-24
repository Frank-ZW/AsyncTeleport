package net.craftgalaxy.asyncteleport.listener.listeners;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.listener.BukkitListener;
import net.craftgalaxy.asyncteleport.manager.TeleportManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends BukkitListener<PlayerQuitEvent> {

    public PlayerQuitListener(AsyncTeleport plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void handleEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        TeleportManager.getInstance().removeTeleportTask(player);
    }
}
