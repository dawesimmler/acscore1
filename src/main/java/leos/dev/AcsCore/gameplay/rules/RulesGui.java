package leos.dev.AcsCore.gameplay.rules;

import leos.dev.AcsCore.util.ColorUtil;
import leos.dev.AcsCore.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class RulesGui {
    public void open(Player p) {
        Inventory gui = Bukkit.createInventory(null,27, ColorUtil.color("&#DEB1FB&lAcsPvP Rules"));
        gui.setItem(11, new ItemBuilder(Material.BOOK).name("&#DEB1FB&lChat").lore(List.of(
                "&r",
                "&8• &7Be respectful",
                "&8• &7No excessive toxicity",
                "&8• &7No hate speech",
                "&8• &7No ads",
                "&8• &7Follow staff"
        )).build());
        gui.setItem(13, new ItemBuilder(Material.BOOK).name("&#DEB1FB&lCheating").lore(List.of(
                "&r",
                "&8• &7No hacked clients",
                "&8• &7No macros/ghost",
                "&8• &7No illegal mods",
                "&8• &7Fair play"
        )).build());
        gui.setItem(15, new ItemBuilder(Material.BOOK).name("&#DEB1FB&lGameplay").lore(List.of(
                "&r",
                "&8• &7No bug abuse",
                "&8• &7Respect PvP zones",
                "&8• &7No grief/troll"
        )).build());
        p.openInventory(gui);
    }
}