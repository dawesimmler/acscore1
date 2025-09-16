package leos.dev.AcsCore.commands.queue;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.queue.QueueManager;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class QueueCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final QueueManager qm;

    public QueueCommand(AcsCore plugin, QueueManager qm) {
        this.plugin = plugin;
        this.qm = qm;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (qm.contains(p.getUniqueId())) {
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fAlready in queue (&a" + qm.size() + "&f)"));
            return true;
        }
        qm.join(p.getUniqueId());
        p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&fJoined queue (&a" + qm.size() + "&f)"));
        return true;
    }
}