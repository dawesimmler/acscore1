package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class ClearCommand implements CommandExecutor {
    private final AcsCore plugin;

    public ClearCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }
        PlayerInventory inv = p.getInventory();
        inv.clear();
        p.getEnderChest().close();
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fInventory cleared."));
        return true;
    }
}