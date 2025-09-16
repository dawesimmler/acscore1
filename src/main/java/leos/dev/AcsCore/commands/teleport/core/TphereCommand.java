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

public class TphereCommand implements CommandExecutor {
    private final AcsCore plugin;

    public TphereCommand(AcsCore plugin) {
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
            self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /tphere <player> [player2...]"));
            return true;
        }

        List<Player> targets = new ArrayList<>();
        for (String arg : args) {
            Player target = find(arg);
            if (target == null) {
                self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player " + arg + " not found"));
                return true;
            }
            targets.add(target);
        }

        for (Player target : targets) {
            target.teleport(self.getLocation());
            target.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fTeleported to &a" + self.getName()));
        }

        self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fTeleported &a" + targets.size() + " &fplayer(s) to you"));
        return true;
    }
}