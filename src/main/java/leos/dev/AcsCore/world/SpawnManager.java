package leos.dev.AcsCore.world;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnManager {
    private final AcsCore plugin;

    public SpawnManager(AcsCore plugin) {
        this.plugin = plugin;
    }

    public Location getSpawn() {
        String wName = plugin.getConfig().getString("spawn.world","world");
        World w = Bukkit.getWorld(wName);
        if (w == null) return Bukkit.getWorlds().get(0).getSpawnLocation();
        double x = plugin.getConfig().getDouble("spawn.x",0);
        double y = plugin.getConfig().getDouble("spawn.y",64);
        double z = plugin.getConfig().getDouble("spawn.z",0);
        float yaw = (float) plugin.getConfig().getDouble("spawn.yaw",0);
        float pitch = (float) plugin.getConfig().getDouble("spawn.pitch",0);
        return new Location(w,x,y,z,yaw,pitch);
    }

    public void teleportToSpawn(Player p, boolean resetFall) {
        Location loc = getSpawn();
        p.teleport(loc);
        if (resetFall) p.setFallDistance(0f);
    }
}