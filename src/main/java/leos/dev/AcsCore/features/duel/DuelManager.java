package leos.dev.AcsCore.features.duel;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class DuelManager {
    private final Map<UUID, DuelRequest> requests = new HashMap<>();
    private final AcsCore plugin;

    public DuelManager(AcsCore plugin) {
        this.plugin = plugin;
    }

    public void put(DuelRequest r) {
        requests.put(r.target, r);
    }

    public DuelRequest get(UUID target) {
        DuelRequest r = requests.get(target);
        if (r != null && r.expired()) {
            requests.remove(target);
            return null;
        }
        return r;
    }

    public void remove(UUID target) {
        requests.remove(target);
    }

    public void start(Player a, Player b, String world) {
        World w = Bukkit.getWorld(world);
        if (w == null) {
            a.sendMessage("World not found");
            b.sendMessage("World not found");
            return;
        }
        Random rand = new Random();
        int max = 200000;
        int x = rand.nextInt(max*2)-max;
        int z = rand.nextInt(max*2)-max;
        double d = 10.0;
        int y1 = w.getHighestBlockYAt(x,z)+1;
        int z2 = z + (int)d;
        int y2 = w.getHighestBlockYAt(x,z2)+1;
        Location l1 = new Location(w,x+0.5,y1,z+0.5);
        Location l2 = new Location(w,x+0.5,y2,z2+0.5);
        l1.setDirection(l2.toVector().subtract(l1.toVector()));
        l2.setDirection(l1.toVector().subtract(l2.toVector()));
        a.teleport(l1);
        b.teleport(l2);
        a.sendMessage("Duel started");
        b.sendMessage("Duel started");
    }
}