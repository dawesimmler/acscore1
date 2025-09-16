package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    private final AcsCore plugin;

    public PingCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private int ping(Player p) {
        return p.getPing();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fYour ping: &a" + ping(p) + "ms"));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player not found"));
            return true;
        }
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fPing of &a" + target.getName() + "&f: &a" + ping(target) + "ms"));
        return true;
    }
}