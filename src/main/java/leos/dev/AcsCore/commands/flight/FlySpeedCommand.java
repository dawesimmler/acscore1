package leos.dev.AcsCore.commands.flight;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class FlySpeedCommand implements CommandExecutor {
    private final AcsCore plugin;

    public FlySpeedCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private float map(int level) {
        if (level < 1) level = 1;
        if (level > 10) level = 10;
        return 0.1f + (level-1)*0.1f;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }
        if (args.length != 1) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fUsage: /flyspeed <1-10>"));
            return true;
        }
        try {
            int lvl = Integer.parseInt(args[0]);
            float speed = map(lvl);
            p.setFlySpeed(Math.min(speed,1f));
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fFly speed &a" + lvl));
        } catch (Exception ex) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Invalid number"));
        }
        return true;
    }
}