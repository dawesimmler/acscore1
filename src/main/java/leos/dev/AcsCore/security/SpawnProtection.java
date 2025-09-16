package leos.dev.AcsCore.security;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SpawnProtection implements Listener {
    private final AcsCore plugin;

    public SpawnProtection(AcsCore plugin) {
        this.plugin = plugin;
    }

    private boolean isProtectedWorld(Player p) {
        if (!plugin.getConfig().getBoolean("spawn.protect-world", true)) return false;
        String world = plugin.getConfig().getString("spawn.world","world");
        return p.getWorld().getName().equalsIgnoreCase(world);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!plugin.getConfig().getBoolean("spawn.protect-build", true)) return;
        if (isProtectedWorld(e.getPlayer()) && !e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (!plugin.getConfig().getBoolean("spawn.protect-build", true)) return;
        if (isProtectedWorld(e.getPlayer()) && !e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (!plugin.getConfig().getBoolean("spawn.protect-explosions", true)) return;
        String world = plugin.getConfig().getString("spawn.world","world");
        if (e.getEntity() != null && e.getEntity().getWorld().getName().equalsIgnoreCase(world)) e.blockList().clear();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (!isProtectedWorld(p)) return;
        if (p.isOp()) return;
        for (String cmd : plugin.getConfig().getStringList("spawn.restricted-commands")) {
            if (e.getMessage().toLowerCase().startsWith(cmd.toLowerCase())) {
                e.setCancelled(true);
                return;
            }
        }
    }
}