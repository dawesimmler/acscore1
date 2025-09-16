package leos.dev.AcsCore.commands.queue;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.queue.QueueManager;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class LeaveQueueCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final QueueManager qm;

    public LeaveQueueCommand(AcsCore plugin, QueueManager qm) {
        this.plugin = plugin;
        this.qm = qm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (!qm.leave(p.getUniqueId())) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Not in queue"));
            return true;
        }
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fLeft queue"));
        return true;
    }
}