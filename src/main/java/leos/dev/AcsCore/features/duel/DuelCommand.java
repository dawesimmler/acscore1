package leos.dev.AcsCore.features.duel;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final DuelManager manager;
    private final long timeoutMs = 15000;

    public DuelCommand(AcsCore plugin, DuelManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (args.length != 1) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /duel <player>"));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player not found"));
            return true;
        }
        if (target == p) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Cannot self"));
            return true;
        }
        DuelRequest existing = manager.get(target.getUniqueId());
        if (existing != null && !existing.expired()) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Already pending"));
            return true;
        }
        DuelRequest req = new DuelRequest(p.getUniqueId(), target.getUniqueId(), System.currentTimeMillis() + timeoutMs);
        manager.put(req);
        p.sendMessage(ColorUtil.color("&#DEB1FBYou sent a duel request to &#00FF00" + target.getName() + "&#DEB1FB! They have &a15 seconds &7to accept!"));
        TextComponent msg = new TextComponent(p.getName() + " is requesting to duel you! ");
        msg.setColor(ChatColor.of("#DEB1FB"));
        TextComponent acceptBtn = new TextComponent("[Accept]");
        acceptBtn.setColor(ChatColor.GREEN);
        acceptBtn.setBold(true);
        acceptBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duelaccept"));
        TextComponent denyBtn = new TextComponent("[Deny]");
        denyBtn.setColor(ChatColor.RED);
        denyBtn.setBold(true);
        denyBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dueldeny"));
        msg.addExtra(acceptBtn);
        msg.addExtra(new TextComponent(" "));
        msg.addExtra(denyBtn);
        target.spigot().sendMessage(msg);
        new BukkitRunnable() {
            @Override
            public void run() {
                DuelRequest check = manager.get(target.getUniqueId());
                if (check != null && check == req && check.expired()) {
                    manager.remove(target.getUniqueId());
                    p.sendMessage(ColorUtil.color("&#FB2626Your duel request to &#00FF00" + target.getName() + " &#FB2626expired."));
                }
            }
        }.runTaskLater(plugin, 15*20);
        return true;
    }
}