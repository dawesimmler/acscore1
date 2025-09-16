package leos.dev.AcsCore.commands.messaging;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.messaging.PrivateMessageManager;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ReplyCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final PrivateMessageManager pm;

    public ReplyCommand(AcsCore plugin, PrivateMessageManager pm) {
        this.plugin = plugin;
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (args.length < 1) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /r <message>"));
            return true;
        }
        var id = pm.last(p.getUniqueId());
        if (id == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No one to reply to"));
            return true;
        }
        Player target = Bukkit.getPlayer(id);
        if (target == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626That player is offline"));
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (String a : args) sb.append(a).append(" ");
        String msg = sb.toString().trim();
        p.sendMessage(ColorUtil.color("&7[&#DEB1FBYou &7&#DEB1FB" + target.getName() + "&7] &f" + msg));
        target.sendMessage(ColorUtil.color("&7[&#DEB1FB" + p.getName() + " &7&#DEB1FBYou&7] &f" + msg));
        pm.link(p.getUniqueId(), target.getUniqueId());
        return true;
    }
}