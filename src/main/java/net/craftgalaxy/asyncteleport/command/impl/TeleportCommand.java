package net.craftgalaxy.asyncteleport.command.impl;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.WildernessPerms;
import net.craftgalaxy.asyncteleport.command.ICommand;
import net.craftgalaxy.asyncteleport.manager.TeleportManager;
import net.craftgalaxy.asyncteleport.runnable.TeleportRunnable;
import net.craftgalaxy.asyncteleport.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class TeleportCommand implements ICommand {

    private final AsyncTeleport plugin;

    public TeleportCommand(AsyncTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(String.format("%s" + ChatColor.RED + " You must be a player to run this command.", this.plugin.getFormattedName()));
            return;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(WildernessPerms.TELEPORT.getPermission())) {
            player.sendMessage(StringUtil.INSUFFICIENT_PERMISSION);
            return;
        }

        if (TeleportManager.getInstance().containsPlayerCooldown(player) && !player.hasPermission(WildernessPerms.BYPASS.getPermission())) {
            long secondsLeft = (player.hasPermission(WildernessPerms.DONATOR.getPermission()) ? this.plugin.getDonatorCooldown() : this.plugin.getDefaultCooldown()) - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - TeleportManager.getInstance().getCooldown(player));
            if (secondsLeft >= 0) {
                player.sendMessage(String.format("%s" + ChatColor.GRAY + " You have " + ChatColor.WHITE + "%s" + ChatColor.GRAY + " seconds left before you can run this command.", this.plugin.getFormattedName(), secondsLeft));
                return;
            } else {
                TeleportManager.getInstance().removeCooldown(player);
            }
        }

        player.sendMessage(String.format("%s" + ChatColor.GRAY + " Generating wilderness location... please be patient, this may take a while.", this.plugin.getFormattedName()));
        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new TeleportRunnable(this.plugin, player));
        TeleportManager.getInstance().addTeleportTask(player, task);
        if (!player.hasPermission(WildernessPerms.BYPASS.getPermission())) {
            TeleportManager.getInstance().addCooldown(player);
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> TeleportManager.getInstance().removeCooldown(player), 20 * (player.hasPermission(WildernessPerms.DONATOR.getPermission()) ? this.plugin.getDonatorCooldown() : this.plugin.getDefaultCooldown()));
    }
}
