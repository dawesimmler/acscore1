package leos.dev.AcsCore.listeners;

import leos.dev.AcsCore.commands.inventory.InvseeCommand;
import leos.dev.AcsCore.commands.inventory.InvseeHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InvseeListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        InvseeCommand.handleClose(e);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof InvseeHolder) {
            InvseeCommand.handleOnlineClick((org.bukkit.entity.Player) e.getWhoClicked(), e.getInventory());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof InvseeHolder) {
            InvseeCommand.handleOnlineClick((org.bukkit.entity.Player) e.getWhoClicked(), e.getInventory());
        }
    }
}