package net.craftgalaxy.asyncteleport.listener.listeners;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.WildernessPerms;
import net.craftgalaxy.asyncteleport.listener.BukkitListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Objects;

public class WildernessSignPlaceListener extends BukkitListener<SignChangeEvent> {

    public WildernessSignPlaceListener(AsyncTeleport plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void handleEvent(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission(WildernessPerms.SIGN.getPermission())) {
            if (e.getLine(0) != null && Objects.requireNonNull(e.getLine(0)).equalsIgnoreCase("[Wilderness]")) {
                e.setLine(0, "");
                e.setLine(1, ChatColor.DARK_BLUE + "[Warp]");
                e.setLine(2, ChatColor.BLUE + this.plugin.getWorld().getName());
                player.sendMessage(String.format("%s" + ChatColor.GREEN + " Successfully created wilderness sign.", this.plugin.getFormattedName()));
            }
        }
    }
}
