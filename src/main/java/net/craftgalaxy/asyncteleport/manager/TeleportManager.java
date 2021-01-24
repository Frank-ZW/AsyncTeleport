package net.craftgalaxy.asyncteleport.manager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {

    // Map of the players who have a teleport thread running
    private Map<UUID, BukkitTask> players;

    // Map of the players and their respective cooldowns
    private final Map<UUID, Long> cooldowns;

    // Singleton instance
    private static TeleportManager instance;

    public TeleportManager() {
        this.players = new HashMap<>();
        this.cooldowns = new HashMap<>();
    }

    public static void enable() {
        TeleportManager.instance = new TeleportManager();
    }

    public static void disable() {
        TeleportManager.instance.cancelPendingTasks();
        TeleportManager.instance.players = null;
        TeleportManager.instance = null;
    }

    public static TeleportManager getInstance() {
        return instance;
    }

    /*
     * Loops through the map and cancels every ongoing task. This is only
     * called when the plugin is disabled.
     */
    public void cancelPendingTasks() {
        this.players.forEach(((uuid, task) -> {
            this.players.remove(uuid);
            task.cancel();
        }));
    }

    public void addTeleportTask(Player player, BukkitTask task) {
        this.players.put(player.getUniqueId(), task);
    }

    public void removeTeleportTask(Player player) {
        BukkitTask task = this.players.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public void addCooldown(Player player) {
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void removeCooldown(Player player) {
        this.cooldowns.remove(player.getUniqueId());
    }

    public Long getCooldown(Player player) {
        return this.cooldowns.get(player.getUniqueId());
    }

    public boolean containsPlayerCooldown(Player player) {
        return this.cooldowns.containsKey(player.getUniqueId());
    }
}
