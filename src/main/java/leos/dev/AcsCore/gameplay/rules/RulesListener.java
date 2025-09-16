package leos.dev.AcsCore.gameplay.rules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RulesListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().contains("AcsPvP Rules")) e.setCancelled(true);
    }
}