package net.craftgalaxy.asyncteleport.command.impl;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.WildernessPerms;
import net.craftgalaxy.asyncteleport.command.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements ICommand {

    private final AsyncTeleport plugin;

    public ReloadCommand(AsyncTeleport plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(WildernessPerms.STAFF.getPermission())) {
            sender.sendMessage(String.format("%s" + ChatColor.RED + " You do not have permission to run this command.", this.plugin.getFormattedName()));
            return;
        }

        this.plugin.loadFromConfig();
        sender.sendMessage(String.format("%s" + ChatColor.GREEN + " Config.yml has been successfully reloaded!", this.plugin.getFormattedName()));
    }
}
