package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AcscoreRootCommand implements CommandExecutor {
    private final AcsCore plugin;

    public AcscoreRootCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fAcsCore running."));
        return true;
    }
}