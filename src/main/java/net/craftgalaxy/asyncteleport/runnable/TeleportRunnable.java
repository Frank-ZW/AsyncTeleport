package net.craftgalaxy.asyncteleport.runnable;

import net.craftgalaxy.asyncteleport.AsyncTeleport;
import net.craftgalaxy.asyncteleport.manager.TeleportManager;
import net.craftgalaxy.asyncteleport.util.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class TeleportRunnable implements Runnable {

    /*
     * This runnable is used to generate a random location to teleport the player and, if the player is on, teleport
     * them to the location. This is supposed to be run asynchronously to prevent overhead on the main server thread
     * to cause lag. While the action of teleporting a player to a specific set of coordinates can only be done
     * synchronously, the work done to find a suitable location can be done separately.
     */

    private boolean foundChunk;
    private final Player player;
    private final AsyncTeleport plugin;

    public TeleportRunnable(AsyncTeleport plugin, Player player) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        while (!this.foundChunk) {
            int x = ThreadLocalRandom.current().nextInt(this.plugin.getMinX(), this.plugin.getMaxX() + 1);
            int z = ThreadLocalRandom.current().nextInt(this.plugin.getMinZ(), this.plugin.getMaxZ() + 1);
            try {
                Chunk chunk = this.plugin.getWorld().getChunkAtAsync(this.plugin.getWorld().getHighestBlockAt(x, z)).get();

                /*
                 * It is much more efficient to check the surrounding blocks within a chunk, a 16 x 16 square plot of land that defines
                 * all the blocks within that area, instead of checking whether a single block is a valid location to teleport to. That way,
                 * the plugin will allow the player to be teleported to beaches and shallow oceans - regions that are close to land but are
                 * still considered "oceans." This is to reduce the amount of time it takes for the plugin to generate a location and makes
                 * the plugin run much faster.
                 */
                Location blockLocation = ChunkUtils.getChunkLandLoc(chunk);
                if (blockLocation != null) {
                    this.foundChunk = true;
                    TeleportManager.getInstance().removeTeleportTask(player);
                    /*
                     * Since this is running on a separate thread, the runnable will create a new scheduler on the main server
                     * thread that will tell the server to teleport the player, if they are online, to the location specified.
                     * Calling the #teleport method without running a new task on the scheduler would have thrown an exception.
                     */
                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        Location teleportLocation = blockLocation.clone().add(0.5, 2, 0.5);
                        if (player.isOnline()) {
                            player.teleport(teleportLocation);
                            player.sendMessage(String.format("%s" + ChatColor.GRAY + " Teleported you to (%s, %s, %s).", plugin.getFormattedName(), teleportLocation.getBlockX(), teleportLocation.getBlockY(), teleportLocation.getBlockZ()));
                        }
                    });
                }
            } catch (InterruptedException | ExecutionException e) {
                player.sendMessage(String.format("%s" + ChatColor.RED + " There was an error generating a location, please try again.", this.plugin.getFormattedName()));
                Bukkit.getLogger().log(Level.SEVERE, "Failed to retrieve chunk asynchronously for " + this.player.getDisplayName() + ChatColor.RESET + " ", e);
            }
        }
    }
}
