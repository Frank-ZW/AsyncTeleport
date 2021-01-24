package net.craftgalaxy.asyncteleport;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.craftgalaxy.asyncteleport.command.CommandHandler;
import net.craftgalaxy.asyncteleport.command.impl.InfoCommand;
import net.craftgalaxy.asyncteleport.command.impl.ReloadCommand;
import net.craftgalaxy.asyncteleport.command.impl.TeleportCommand;
import net.craftgalaxy.asyncteleport.listener.BukkitListener;
import net.craftgalaxy.asyncteleport.listener.listeners.PlayerQuitListener;
import net.craftgalaxy.asyncteleport.listener.listeners.WildernessSignListener;
import net.craftgalaxy.asyncteleport.listener.listeners.WildernessSignPlaceListener;
import net.craftgalaxy.asyncteleport.manager.TeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class AsyncTeleport extends JavaPlugin {

    private CommandHandler commandHandler;

    // The world the plugin should randomly teleport the player to
    // In the future, I will consider adding teleportation support
    // to other worlds that can be edited int he config.yml file
    private World world;

    /*
     * The region the plugin selects from is a rectangular box, whose lower left-hand
     * corner is denoted by the coordinates (minX, minZ) and upper right-hand corner
     * is denoted by the coordinates (maxX, maxZ). The plugin chooses an arbitrary
     * location within that region and checks if the location is either not in an ocean
     * and is not in lava. If both conditions are satisfied, the plugin teleports the
     * player and applies a cooldown.
     */
    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;

    /*
     * The default and donator cooldown. Donators receive a shorter cooldown for
     * donating to our server and helping us keep the server running.
     */
    private int defaultCooldown;
    private int donatorCooldown;

    // Singleton instance of the plugin
    private static AsyncTeleport instance;

    private final Class<? extends BukkitListener<?>>[] listenerClasses = new Class[] {
            PlayerQuitListener.class,
            WildernessSignListener.class,
            WildernessSignPlaceListener.class
    };

    private Set<BukkitListener<?>> listenerInstances;

    @Override
    public void onEnable() {
        instance = this;
        TeleportManager.enable();
        this.registerListeners();
        this.registerCommands();
        if (this.loadFromConfig()) {
            Bukkit.getPluginManager().disablePlugin(this);  // Disables this plugin if the startup failed
        }
    }

    @Override
    public void onDisable() {
        this.unregisterListeners();
        this.unregisterCommands();
        TeleportManager.disable();
        Bukkit.getScheduler().cancelTasks(this);
        instance = null;
    }

    // Since I made separate classes for each listener, we have to loop through the listener classes and
    // instantiate each class and add the listener to the server
    public void registerListeners() {
        this.listenerInstances = new HashSet<>();
        Arrays.stream(this.listenerClasses).forEach(clazz -> {
            try {
                BukkitListener<?> listenerInstance = clazz.asSubclass(BukkitListener.class).getConstructor(AsyncTeleport.class).newInstance(this);
                this.listenerInstances.add(listenerInstance);
                Bukkit.getPluginManager().registerEvents(listenerInstance, this);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        });
    }

    // This is good just to prevent memory leaks
    public void unregisterListeners() {
        this.listenerInstances.forEach(BukkitListener::disable);
        this.listenerInstances.clear();
        this.listenerInstances = null;
    }

    // Returns the official server in-game name with appropriate color codes and brackets
    public String getFormattedName() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Craft" + ChatColor.BLUE + "Galaxy" + ChatColor.DARK_GRAY + "]";
    }

    // Registers the possible commands for this plugin
    public void registerCommands() {
        this.commandHandler = new CommandHandler(this);
        this.commandHandler.register("rtp", new TeleportCommand(this));     // Handles all random teleportation
        this.commandHandler.register("info", new InfoCommand(this));        // Reports information on successful and failed teleportations
        this.commandHandler.register("reload", new ReloadCommand(this));    // Reloads the plugin; much safer than using a third-party plugin to restart this plugin

        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(this.commandHandler);
    }

    public void unregisterCommands() {
        this.commandHandler.disable();
    }

    // Loads the information from the config.yml file
    public boolean loadFromConfig() {
        /*
         * If the world listed in the configuration file is not found or does not exist, the plugin will
         * automatically disable itself after starting up to ensure no errors are thrown every time a player
         * tries randomly teleporting and that all the appropriate files are loaded in before the plugin
         * disables itself.
         */
        boolean shutdown = false;
        this.saveDefaultConfig();
        String worldName = this.getConfig().getString("wilderness-world-name");
        if (worldName == null) {
            Bukkit.getLogger().info(String.format("%s" + ChatColor.GRAY + " Please specify a world name to teleport to!", this.getFormattedName()));
            shutdown = true;
        } else {
            this.world = Bukkit.getWorld(worldName);
        }

        // Makes it much more convenient for the person entering coordinates into the config.yml
        // Simply finds the smallest x and z values to be stored into minX, minZ, maxX, and maxZ
        int x1 = this.getConfig().getInt("coordinates.x1");
        int z1 = this.getConfig().getInt("coordinates.z1");
        int x2 = this.getConfig().getInt("coordinates.x2");
        int z2 = this.getConfig().getInt("coordinates.z2");
        this.minX = Math.min(x1, x2);
        this.minZ = Math.min(z1, z2);
        this.maxX = this.minX == x1 ? x2 : x1;
        this.maxZ = this.minZ == z1 ? z2 : z1;
        this.defaultCooldown = this.getConfig().getInt("cooldowns.default-duration");
        this.donatorCooldown = this.getConfig().getInt("cooldowns.donator-duration");

        return shutdown;
    }

    public World getWorld() {
        return world;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getDefaultCooldown() {
        return defaultCooldown;
    }

    public int getDonatorCooldown() {
        return donatorCooldown;
    }

    public static AsyncTeleport getInstance() {
        return instance;
    }
}
