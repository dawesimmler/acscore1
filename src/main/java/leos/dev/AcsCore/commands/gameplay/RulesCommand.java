package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.gameplay.rules.RulesGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        new RulesGui().open(p);
        return true;
    }
}