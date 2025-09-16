package leos.dev.AcsCore.listeners;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import leos.dev.AcsCore.world.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CoreJoinQuitListener implements Listener {
    private final AcsCore plugin;

    public CoreJoinQuitListener(AcsCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String plus = plugin.getConfig().getString("chat.join-plus","[+] %player%").replace("%player%", p.getName());
        e.setJoinMessage(ColorUtil.color(plus));
        if (!p.hasPlayedBefore()) {
            Bukkit.getScheduler().runTask(plugin, () -> plugin.spawnManager().teleportToSpawn(p, true));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String minus = plugin.getConfig().getString("chat.join-minus","[-] %player%").replace("%player%", e.getPlayer().getName());
        e.setQuitMessage(ColorUtil.color(minus));
    }
}