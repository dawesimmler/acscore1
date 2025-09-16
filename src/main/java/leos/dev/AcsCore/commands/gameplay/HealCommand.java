package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    private final AcsCore plugin;

    public HealCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private void heal(Player t) {
        double max = t.getMaxHealth();
        t.setHealth(max);
        t.setFoodLevel(20);
        t.setSaturation(14f);
        t.setFireTicks(0);
        t.setRemainingAir(t.getMaximumAir());
        t.setFreezeTicks(0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }

        if (args.length == 0) {
            heal(p);
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fYou have been healed."));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Player not found"));
            return true;
        }

        heal(target);
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fYou healed &d" + target.getName() + "&f."));
        if (target != p) {
            target.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fYou have been healed by &d" + p.getName() + "&f."));
        }
        return true;
    }
}
