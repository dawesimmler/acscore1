package leos.dev.AcsCore.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Material m) {
        this.stack = new ItemStack(m);
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ColorUtil.color(name));
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lines.stream().map(ColorUtil::color).toList());
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flags);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return stack;
    }
}