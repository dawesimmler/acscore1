package leos.dev.AcsCore.commands.gameplay;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {
    private final AcsCore plugin;

    public DiscordCommand(AcsCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only");
            return true;
        }
        for (String l : plugin.getConfig().getStringList("notify.discord-lines")) {
            p.sendMessage(ColorUtil.color(l));
        }
        return true;
    }
}