package net.craftgalaxy.asyncteleport.command;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private AsyncTeleport plugin;
    private Map<String, ICommand> commands;

    public CommandHandler(AsyncTeleport plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<>();
    }

    public ICommand getExecutor(String name) {
        return this.commands.get(name);
    }

    public void register(String name, ICommand command) {
        this.commands.put(name, command);
    }

    public void disable() {
        this.commands.clear();
        this.commands = null;
        this.plugin = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.getExecutor("rtp").onCommand(sender, args);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                this.getExecutor("reload").onCommand(sender, args);
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                this.getExecutor("info").onCommand(sender, args);
                return true;
            }
        }

        sender.sendMessage(String.format("%s" + ChatColor.RED + " This command does not exist.", this.plugin.getFormattedName()));
        return true;
    }
}
