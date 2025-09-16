package leos.dev.AcsCore.commands.messaging;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.messaging.PrivateMessageManager;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final PrivateMessageManager pm;

    public MsgCommand(AcsCore plugin, PrivateMessageManager pm) {
        this.plugin = plugin;
        this.pm = pm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (args.length < 2) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /msg <player> <message>"));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player not found"));
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i=1;i<args.length;i++) sb.append(args[i]).append(" ");
        String msg = sb.toString().trim();
        p.sendMessage(ColorUtil.color("&7[&#DEB1FBYou &7&#DEB1FB" + target.getName() + "&7] &f" + msg));
        target.sendMessage(ColorUtil.color("&7[&#DEB1FB" + p.getName() + " &7&#DEB1FBYou&7] &f" + msg));
        pm.link(p.getUniqueId(), target.getUniqueId());
        return true;
    }
}