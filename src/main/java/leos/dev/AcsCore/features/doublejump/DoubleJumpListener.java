package leos.dev.AcsCore.features.doublejump;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DoubleJumpListener implements Listener {
    private final AcsCore plugin;
    private final Map<UUID, Long> cd = new ConcurrentHashMap<>();

    public DoubleJumpListener(AcsCore plugin) {
        this.plugin = plugin;
    }

    private boolean enabled() {
        return plugin.getConfig().getBoolean("doublejump.enabled", true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!enabled()) return;
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) return;
        if (worldMatch(p)) p.setAllowFlight(true);
    }

    private boolean worldMatch(Player p) {
        String w = plugin.getConfig().getString("doublejump.world","world");
        return p.getWorld().getName().equalsIgnoreCase(w);
    }

    @EventHandler
    public void onToggle(PlayerToggleFlightEvent e) {
        if (!enabled()) return;
        Player p = e.getPlayer();
        if (!worldMatch(p)) return;
        if (p.getGameMode() == GameMode.CREATIVE) return;
        long now = System.currentTimeMillis();
        long last = cd.getOrDefault(p.getUniqueId(),0L);
        long cooldown = plugin.getConfig().getLong("doublejump.cooldown-ms",1000);
        if (now - last < cooldown) {
            e.setCancelled(true);
            return;
        }
        cd.put(p.getUniqueId(), now);
        e.setCancelled(true);
        p.setFlying(false);
        p.setAllowFlight(false);
        double f = plugin.getConfig().getDouble("doublejump.velocity.forward-multiplier",1.7);
        double vy = plugin.getConfig().getDouble("doublejump.velocity.y",1.1);
        Vector v = p.getLocation().getDirection().multiply(f).setY(vy);
        p.setVelocity(v);
        String sound = plugin.getConfig().getString("doublejump.sound","ENTITY_FIREWORK_ROCKET_BLAST");
        try {
            p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 1f,1f);
        } catch (Exception ignored) {}
        String particleType = plugin.getConfig().getString("doublejump.particles.type","CLOUD");
        int count = plugin.getConfig().getInt("doublejump.particles.count",20);
        double ox = plugin.getConfig().getDouble("doublejump.particles.offsetX",0.4);
        double oy = plugin.getConfig().getDouble("doublejump.particles.offsetY",0.3);
        double oz = plugin.getConfig().getDouble("doublejump.particles.offsetZ",0.4);
        try {
            p.getWorld().spawnParticle(Particle.valueOf(particleType), p.getLocation(), count, ox, oy, oz, 0.01);
        } catch (Exception ignored) {}
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!p.isOnline()) return;
            if (worldMatch(p) && p.getGameMode() != GameMode.CREATIVE) p.setAllowFlight(true);
        }, 5L);
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (!enabled()) return;
        if (!(e.getEntity() instanceof Player p)) return;
        if (!worldMatch(p)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }
}