package leos.dev.AcsCore.commands.teleport.core;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpCommand implements CommandExecutor {
    private final AcsCore plugin;

    public TpCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private Player find(String name) {
        return Bukkit.getPlayerExact(name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player self)) {
            sender.sendMessage("Players only");
            return true;
        }
        if (args.length == 0) {
            self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /tp <player> OR /tp <p1> <p2> [... chain]"));
            return true;
        }
        if (args.length == 1) {
            Player target = find(args[0]);
            if (target == null) {
                self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player not found"));
                return true;
            }
            self.teleport(target.getLocation());
            self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fTeleported to &a" + target.getName()));
            return true;
        }
        List<Player> list = new ArrayList<>();
        for (String a : args) {
            Player p = find(a);
            if (p == null) {
                self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player " + a + " not found"));
                return true;
            }
            list.add(p);
        }
        Player dest = list.get(list.size()-1);
        for (int i=0;i<list.size()-1;i++) {
            Player p = list.get(i);
            p.teleport(dest.getLocation());
            if (p != dest)
                p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fTeleported to &a" + dest.getName()));
        }
        self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fTeleport chain complete."));
        return true;
    }
}