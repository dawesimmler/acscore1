package leos.dev.AcsCore.world;

import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DeathLocationTracker {
    private final Map<UUID, DeathSite> map = new ConcurrentHashMap<>();

    public void record(UUID player, Location loc) {
        map.put(player, new DeathSite(loc, System.nanoTime()));
    }

    public DeathSite consume(UUID player) {
        return map.remove(player);
    }

    public DeathSite peek(UUID player) {
        return map.get(player);
    }

    public static class DeathSite {
        public final Location location;
        public final long id;
        public DeathSite(Location l, long id) {
            this.location = l.clone();
            this.id = id;
        }
    }
}