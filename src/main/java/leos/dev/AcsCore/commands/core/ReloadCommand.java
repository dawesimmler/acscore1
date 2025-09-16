package leos.dev.AcsCore.commands.core;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final AcsCore plugin;

    public ReloadCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("acscore.admin.reload")) {
            sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626No permission"));
            return true;
        }
        plugin.configService().reload();
        sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&aReloaded."));
        return true;
    }
}