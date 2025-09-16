package leos.dev.AcsCore.listeners;

import leos.dev.AcsCore.commands.inventory.InvseeCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerSnapshotListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        InvseeCommand.saveOnlineSnapshot(e.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        InvseeCommand.applyOnJoin(e.getPlayer());
    }
}