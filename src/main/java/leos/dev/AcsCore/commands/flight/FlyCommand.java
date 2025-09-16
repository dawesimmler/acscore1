package leos.dev.AcsCore.commands.flight;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class FlyCommand implements CommandExecutor {
    private final AcsCore plugin;

    public FlyCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private void toggle(Player p) {
        boolean enable = !p.getAllowFlight();
        p.setAllowFlight(enable);
        p.setFlying(enable);
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fFlight " + (enable ? "&aenabled" : "&cdisabled")));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player self)) {
            sender.sendMessage("Players only");
            return true;
        }
        if (!sender.hasPermission("acscore.flight")) {
            self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No permission"));
            return true;
        }
        if (args.length == 0) {
            toggle(self);
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player not found"));
            return true;
        }
        toggle(target);
        if (target != self) self.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fToggled flight for &a" + target.getName()));
        return true;
    }
}