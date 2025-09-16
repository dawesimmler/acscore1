package leos.dev.AcsCore.commands.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class InvseeHolder implements InventoryHolder {
    private final UUID target;
    private final boolean online;

    public InvseeHolder(UUID target, boolean online) {
        this.target = target;
        this.online = online;
    }

    public UUID getTarget() {
        return target;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}