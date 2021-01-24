package net.craftgalaxy.asyncteleport.command.impl;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.WildernessPerms;
import net.craftgalaxy.asyncteleport.command.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements ICommand {

    private final AsyncTeleport plugin;

    public InfoCommand(AsyncTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(WildernessPerms.STAFF.getPermission())) {
            sender.sendMessage(String.format("%s" + ChatColor.RED + " You do not have permission to run this command.", this.plugin.getFormattedName()));
            return;
        }

        sender.sendMessage(ChatColor.WHITE + "----------" + this.plugin.getFormattedName() + ChatColor.WHITE + "----------");
        sender.sendMessage(ChatColor.BLUE + "World name: " + ChatColor.WHITE + this.plugin.getWorld().getName());
        sender.sendMessage(ChatColor.BLUE + "Bottom left X: " + ChatColor.WHITE + this.plugin.getMinX());
        sender.sendMessage(ChatColor.BLUE + "Bottom left Z: " + ChatColor.WHITE + this.plugin.getMinZ());
        sender.sendMessage(ChatColor.BLUE + "Upper right X: " + ChatColor.WHITE + this.plugin.getMaxX());
        sender.sendMessage(ChatColor.BLUE + "Upper right Z: " + ChatColor.WHITE + this.plugin.getMaxZ());
        sender.sendMessage(ChatColor.BLUE + "Donator cooldown: " + ChatColor.WHITE + this.plugin.getDonatorCooldown() + " seconds");
        sender.sendMessage(ChatColor.BLUE + "Default cooldown: " + ChatColor.WHITE + this.plugin.getDefaultCooldown() + " seconds");
    }
}
