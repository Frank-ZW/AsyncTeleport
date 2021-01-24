package net.craftgalaxy.asyncteleport.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockUtils {

    private static final Set<Material> SIGNS = new HashSet<>();

    static {
        SIGNS.add(Material.OAK_WALL_SIGN);
        SIGNS.add(Material.OAK_SIGN);
        SIGNS.add(Material.JUNGLE_WALL_SIGN);
        SIGNS.add(Material.JUNGLE_SIGN);
        SIGNS.add(Material.DARK_OAK_WALL_SIGN);
        SIGNS.add(Material.DARK_OAK_SIGN);
        SIGNS.add(Material.BIRCH_WALL_SIGN);
        SIGNS.add(Material.BIRCH_SIGN);
        SIGNS.add(Material.SPRUCE_WALL_SIGN);
        SIGNS.add(Material.SPRUCE_SIGN);
        SIGNS.add(Material.ACACIA_WALL_SIGN);
        SIGNS.add(Material.ACACIA_SIGN);
    }

    public static boolean isSign(Block block) {
        return SIGNS.contains(block.getType());
    }
}
