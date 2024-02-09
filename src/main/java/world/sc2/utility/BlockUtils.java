package world.sc2.utility;

import org.bukkit.Location;

import java.util.Collection;
import java.util.HashSet;

public class BlockUtils {
    public static Collection<Location> getBlocksInArea(Location loc1, Location loc2) {
        Collection<Location> blocks = new HashSet<>();
        if (loc1.getWorld() == null) return blocks;

        int topBlockX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int bottomBlockX = Math.min(loc1.getBlockX(), loc2.getBlockX());

        int topBlockY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int bottomBlockY = Math.min(loc1.getBlockY(), loc2.getBlockY());

        int topBlockZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        int bottomBlockZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Location l = new Location(loc1.getWorld(), x, y, z);
                    if (!loc1.getWorld().getBlockAt(l).getType().toString().contains("AIR")) {
                        blocks.add(l);
                    }
                }
            }
        }
        return blocks;
    }

}
