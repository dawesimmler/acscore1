package leos.dev.AcsCore.features.deathmessages;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.Action;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessageListener implements Listener {
    private final AcsCore plugin;

    public DeathMessageListener(AcsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        String original = e.getDeathMessage();
        e.setDeathMessage(null);
        if (original == null || original.isEmpty()) return;
        int radius = plugin.getConfig().getInt("deathmessages.radius",50);
        double r2 = radius * radius;
        Player dead = e.getEntity();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getWorld().equals(dead.getWorld())) continue;
            if (p.getLocation().distanceSquared(dead.getLocation()) <= r2) {
                if (plugin.getConfig().getBoolean("deathmessages.actionbar", true)) {
                    Action.bar(p, "&#DEB1FB" + original);
                } else {
                    p.sendMessage(ColorUtil.color("&#DEB1FB" + original));
                }
            }
        }
    }
}