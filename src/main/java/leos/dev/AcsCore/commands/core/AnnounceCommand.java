package leos.dev.AcsCore.commands.core;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnnounceCommand implements CommandExecutor {
    private final AcsCore plugin;

    public AnnounceCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /announce <text>"));
            return true;
        }
        String msg = String.join(" ", args);
        plugin.announcementService().announce(p, msg);
        return true;
    }
}