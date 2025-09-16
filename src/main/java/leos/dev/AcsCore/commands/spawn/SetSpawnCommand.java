package leos.dev.AcsCore.commands.spawn;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetSpawnCommand implements CommandExecutor {
    private final AcsCore plugin;

    public SetSpawnCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!sender.hasPermission("acscore.spawn.set")) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No permission"));
            return true;
        }
        Location l = p.getLocation();
        plugin.getConfig().set("spawn.world", l.getWorld().getName());
        plugin.getConfig().set("spawn.x", l.getX());
        plugin.getConfig().set("spawn.y", l.getY());
        plugin.getConfig().set("spawn.z", l.getZ());
        plugin.getConfig().set("spawn.yaw", l.getYaw());
        plugin.getConfig().set("spawn.pitch", l.getPitch());
        plugin.saveConfig();
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aSpawn updated"));
        return true;
    }
}