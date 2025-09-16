package leos.dev.AcsCore.commands.teleport.request;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.teleport.request.TpaManager;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpAcceptCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final TpaManager manager;

    public TpAcceptCommand(AcsCore plugin, TpaManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        TpaManager.Request r = manager.get(p.getUniqueId());
        if (r == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No pending request"));
            return true;
        }
        manager.remove(p.getUniqueId());
        Player from = plugin.getServer().getPlayer(r.from);
        if (from == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Requester offline"));
            return true;
        }
        if (r.type == TpaManager.Type.TPA) {
            from.teleport(p.getLocation());
            from.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aTeleporting to " + p.getName()));
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aAccepted"));
        } else {
            p.teleport(from.getLocation());
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aTeleporting to " + from.getName()));
            from.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aAccepted"));
        }
        return true;
    }
}