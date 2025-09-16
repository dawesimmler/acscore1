package leos.dev.AcsCore.commands.inventory;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InvseeCommand implements CommandExecutor {
    private final AcsCore plugin;
    public static final String TITLE_PREFIX = "Inventory of ";
    private static final Map<UUID, ItemStack[]> offlineSnapshots = new HashMap<>();
    private static final Map<UUID, ItemStack[]> offlineArmor = new HashMap<>();
    private static final Map<UUID, ItemStack[]> modifiedOffline = new HashMap<>();

    private static final int SLOT_HELMET = 45;
    private static final int SLOT_CHEST = 46;
    private static final int SLOT_LEGS = 47;
    private static final int SLOT_BOOTS = 48;
    private static final int SLOT_TOTEM = 49;

    public InvseeCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    // === SNAPSHOTS SPEICHERN ===
    public static void saveOnlineSnapshot(Player p) {
        offlineSnapshots.put(p.getUniqueId(), p.getInventory().getContents().clone());
        offlineArmor.put(p.getUniqueId(), p.getInventory().getArmorContents().clone());
    }

    public static void applyOnJoin(Player p) {
        ItemStack[] mod = modifiedOffline.remove(p.getUniqueId());
        if (mod == null) return;
        ItemStack[] contents = Arrays.copyOfRange(mod, 0, 36);
        ItemStack helmet = mod.length > 36 ? mod[36] : null;
        ItemStack chest = mod.length > 37 ? mod[37] : null;
        ItemStack legs = mod.length > 38 ? mod[38] : null;
        ItemStack boots = mod.length > 39 ? mod[39] : null;
        ItemStack totem = mod.length > 40 ? mod[40] : null;

        p.getInventory().setContents(contents);
        p.getInventory().setArmorContents(new ItemStack[]{boots, legs, chest, helmet});
        p.getInventory().setItemInOffHand(totem);
    }

    // === INVENTAR AUFBAUEN ===
    private Inventory buildOffline(UUID id, String name) {
        ItemStack[] base = offlineSnapshots.getOrDefault(id, new ItemStack[36]);
        ItemStack[] armor = offlineArmor.getOrDefault(id, new ItemStack[4]);
        Inventory inv = Bukkit.createInventory(new InvseeHolder(id, false), 54, TITLE_PREFIX + name);

        // Filler zuerst rein
        fillWithGlass(inv);

        for (int i = 0; i < 36; i++) inv.setItem(i, base[i]);
        inv.setItem(SLOT_HELMET, armor.length > 3 ? armor[3] : null);
        inv.setItem(SLOT_CHEST, armor.length > 2 ? armor[2] : null);
        inv.setItem(SLOT_LEGS, armor.length > 1 ? armor[1] : null);
        inv.setItem(SLOT_BOOTS, armor.length > 0 ? armor[0] : null);

        return inv;
    }

    private Inventory buildOnline(Player target) {
        PlayerInventory pi = target.getInventory();
        Inventory inv = Bukkit.createInventory(new InvseeHolder(target.getUniqueId(), true), 54, TITLE_PREFIX + target.getName());

        // Filler zuerst rein
        fillWithGlass(inv);

        for (int i = 0; i < 36; i++) inv.setItem(i, pi.getItem(i));
        ItemStack[] armor = pi.getArmorContents(); // boots, legs, chest, helmet
        ItemStack boots = armor.length > 0 ? armor[0] : null;
        ItemStack legs = armor.length > 1 ? armor[1] : null;
        ItemStack chest = armor.length > 2 ? armor[2] : null;
        ItemStack helmet = armor.length > 3 ? armor[3] : null;
        inv.setItem(SLOT_HELMET, helmet);
        inv.setItem(SLOT_CHEST, chest);
        inv.setItem(SLOT_LEGS, legs);
        inv.setItem(SLOT_BOOTS, boots);
        inv.setItem(SLOT_TOTEM, pi.getItemInOffHand());

        return inv;
    }

    // === INVENTAR SCHLIESSEN / SYNCHRONISIEREN ===
    public static void handleClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof InvseeHolder holder)) return;
        String title = e.getView().getTitle();
        if (!title.startsWith(TITLE_PREFIX)) return;

        if (holder.isOnline()) {
            Player target = Bukkit.getPlayer(holder.getTarget());
            if (target != null) {
                syncGuiToPlayer(e.getInventory(), target);
            }
            return;
        }

        OfflinePlayer op = Bukkit.getOfflinePlayer(holder.getTarget());
        Inventory top = e.getInventory();
        ItemStack[] merged = new ItemStack[41];
        for (int i = 0; i < 36; i++) merged[i] = top.getItem(i);
        merged[36] = top.getItem(SLOT_HELMET);
        merged[37] = top.getItem(SLOT_CHEST);
        merged[38] = top.getItem(SLOT_LEGS);
        merged[39] = top.getItem(SLOT_BOOTS);
        merged[40] = top.getItem(SLOT_TOTEM);

        modifiedOffline.put(op.getUniqueId(), merged);

        if (e.getPlayer() instanceof Player p) {
            p.sendMessage(ColorUtil.color("&aOffline inventory saved."));
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.6f, 1.2f);
        }
    }

    public static void handleOnlineClick(Player viewer, Inventory clicked) {
        if (!(clicked.getHolder() instanceof InvseeHolder holder)) return;
        if (!holder.isOnline()) return;
        Player target = Bukkit.getPlayer(holder.getTarget());
        if (target == null) return;
        Bukkit.getScheduler().runTask(AcsCore.get(), () -> syncGuiToPlayer(clicked, target));
    }

    private static void syncGuiToPlayer(Inventory gui, Player target) {
        ItemStack[] contents = new ItemStack[36];
        for (int i = 0; i < 36; i++) contents[i] = gui.getItem(i);

        ItemStack helmet = gui.getItem(SLOT_HELMET);
        ItemStack chest = gui.getItem(SLOT_CHEST);
        ItemStack legs = gui.getItem(SLOT_LEGS);
        ItemStack boots = gui.getItem(SLOT_BOOTS);
        ItemStack totem = gui.getItem(SLOT_TOTEM);

        target.getInventory().setContents(contents);
        target.getInventory().setArmorContents(new ItemStack[]{boots, legs, chest, helmet});
        target.getInventory().setItemInOffHand(totem);
        target.updateInventory();
    }

    // === FILLER ===
    private void fillWithGlass(Inventory inv) {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§7·"); // kleiner grauer Punkt als Name
            filler.setItemMeta(meta);
        }

        for (int i = 36; i < 54; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }
    }

    // === COMMAND HANDLING ===
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player viewer)) {
            sender.sendMessage("Players only");
            return true;
        }
        if (!viewer.hasPermission("acscore.invsee")) {
            viewer.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&#FB2626No permission"));
            return true;
        }
        if (args.length != 1) {
            viewer.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&fUsage: /invsee <player>"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target != null) {
            Inventory inv = buildOnline(target);
            viewer.openInventory(inv);
            viewer.sendMessage(ColorUtil.color("&7Editing live inventory of &a" + target.getName()));
            viewer.playSound(viewer.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.6f, 1.2f);
            return true;
        }

        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
        if (op.getName() == null) {
            viewer.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&#FB2626Player data not found"));
            return true;
        }
        offlineSnapshots.putIfAbsent(op.getUniqueId(), new ItemStack[36]);
        offlineArmor.putIfAbsent(op.getUniqueId(), new ItemStack[4]);
        Inventory inv = buildOffline(op.getUniqueId(), op.getName());
        viewer.openInventory(inv);
        viewer.sendMessage(ColorUtil.color("&7Viewing offline inventory snapshot of &a" + op.getName()));
        viewer.playSound(viewer.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.6f, 1.2f);
        return true;
    }
}
