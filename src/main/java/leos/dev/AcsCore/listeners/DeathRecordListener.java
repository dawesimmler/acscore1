package leos.dev.AcsCore.listeners;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathRecordListener implements Listener {
    private final AcsCore plugin;

    public DeathRecordListener(AcsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        plugin.deathLocationTracker().record(e.getEntity().getUniqueId(), e.getEntity().getLocation());
    }
}