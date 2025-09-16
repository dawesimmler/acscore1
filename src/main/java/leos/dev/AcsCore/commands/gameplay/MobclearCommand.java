package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class MobclearCommand implements CommandExecutor {
    private final AcsCore plugin;

    public MobclearCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    private boolean isRemovable(Entity e) {
        if (e instanceof Player) return false;
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("acscore.mobclear")) {
            sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No permission"));
            return true;
        }
        int removed = 0;
        for (World w : plugin.getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (isRemovable(e)) {
                    e.remove();
                    removed++;
                }
            }
        }
        sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fRemoved &a" + removed + " &fentities"));
        return true;
    }
}