package leos.dev.AcsCore.security;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSpectatorBlocker implements Listener {
    private final AcsCore plugin;

    public CommandSpectatorBlocker(AcsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPre(PlayerCommandPreprocessEvent e) {
        if (!(e.getPlayer() instanceof Player p)) return;
        if (!plugin.commandRateLimiter().allow(p)) {
            e.setCancelled(true);
            return;
        }
        if (p.getGameMode() == GameMode.SPECTATOR) {
            for (String blocked : plugin.getConfig().getStringList("security.blocked-spectator-commands")) {
                if (e.getMessage().toLowerCase().startsWith(blocked.toLowerCase())) {
                    e.setCancelled(true);
                    p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Cannot use in spectator"));
                    return;
                }
            }
        }
    }
}