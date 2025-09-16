package leos.dev.AcsCore.commands.spawn;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SpawnCommand implements CommandExecutor {
    private final AcsCore plugin;

    public SpawnCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        plugin.spawnManager().teleportToSpawn(p, true);
        return true;
    }
}