package net.craftgalaxy.asyncteleport.listener.listeners;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.WildernessPerms;
import net.craftgalaxy.asyncteleport.listener.BukkitListener;
import net.craftgalaxy.asyncteleport.manager.TeleportManager;
import net.craftgalaxy.asyncteleport.runnable.TeleportRunnable;
import net.craftgalaxy.asyncteleport.util.BlockUtils;
import net.craftgalaxy.asyncteleport.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

public class WildernessSignListener extends BukkitListener<PlayerInteractEvent> {

    public WildernessSignListener(AsyncTeleport plugin) {
        super(plugin);
    }

    @Override
    @EventHandler
    public void handleEvent(PlayerInteractEvent e) {
        Action action = e.getAction();
        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK && BlockUtils.isSign(clickedBlock)) {
            Sign sign = (Sign) clickedBlock.getState();
            if (ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[Warp]") && ChatColor.stripColor(sign.getLine(2)).equalsIgnoreCase(this.plugin.getWorld().getName())) {
                if (!player.hasPermission(WildernessPerms.TELEPORT.getPermission())) {
                    player.sendMessage(StringUtil.INSUFFICIENT_PERMISSION);
                    return;
                }

                player.sendMessage(String.format("%s" + ChatColor.GRAY + " Generating wilderness location... please be patient, this may take a while.", this.plugin.getFormattedName()));
                BukkitTask teleportTask = Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new TeleportRunnable(this.plugin, player));
                TeleportManager.getInstance().addTeleportTask(player, teleportTask);
            }
        }
    }
}
