package net.craftgalaxy.asyncteleport.util;

import org.bukkit.*;
import org.bukkit.block.Block;

import javax.annotation.Nullable;

public class ChunkUtils {

    @Nullable
    public static Location getChunkLandLoc(Chunk chunk) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int x = chunk.getX() * 16 + i;
                int z = chunk.getZ() * 16 + j;
                Block block = chunk.getWorld().getHighestBlockAt(x, z);
                if (ChunkUtils.isValidMaterial(block.getType())) {
                    return block.getLocation();
                }
            }
        }

        return null;
    }

    public static boolean isValidMaterial(Material material) {
        return material != Material.WATER && material != Material.LAVA && material != Material.KELP_PLANT && material != Material.KELP && material != Material.BUBBLE_COLUMN && material != Material.SEAGRASS && material != Material.TALL_SEAGRASS && material != Material.SEA_PICKLE;
    }
}
