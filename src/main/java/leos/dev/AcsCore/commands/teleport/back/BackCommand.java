package leos.dev.AcsCore.commands.teleport.back;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import leos.dev.AcsCore.world.DeathLocationTracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BackCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final Set<String> used = new HashSet<>();

    public BackCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private String key(UUID u, long id) {
        return u.toString()+":"+id;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }
        DeathLocationTracker.DeathSite site = plugin.deathLocationTracker().peek(p.getUniqueId());
        if (site == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No death location"));
            return true;
        }
        String k = key(p.getUniqueId(), site.id);
        if (used.contains(k)) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Already used for this death"));
            return true;
        }
        p.teleport(site.location);
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aReturned to death location"));
        used.add(k);
        return true;
    }
}