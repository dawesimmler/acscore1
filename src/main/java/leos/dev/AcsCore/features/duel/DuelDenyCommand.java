package leos.dev.AcsCore.features.duel;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DuelDenyCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final DuelManager manager;

    public DuelDenyCommand(AcsCore plugin, DuelManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player target)) return true;
        DuelRequest req = manager.get(target.getUniqueId());
        if (req == null) {
            target.sendMessage(ColorUtil.color("&#FB2626No duel request to deny."));
            return true;
        }
        Player challenger = Bukkit.getPlayer(req.challenger);
        if (challenger != null) challenger.sendMessage(ColorUtil.color("&#FB2626Your duel request was denied by " + target.getName() + "."));
        target.sendMessage(ColorUtil.color("&#FB2626You have denied the duel request."));
        manager.remove(target.getUniqueId());
        return true;
    }
}