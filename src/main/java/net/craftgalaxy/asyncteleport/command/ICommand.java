package net.craftgalaxy.asyncteleport.command;

import org.bukkit.command.CommandSender;

public interface ICommand {

    void onCommand(CommandSender sender, String[] args);
}
