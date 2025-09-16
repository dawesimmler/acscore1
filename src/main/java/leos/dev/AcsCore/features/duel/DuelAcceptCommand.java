package leos.dev.AcsCore.features.duel;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class DuelAcceptCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final DuelManager manager;

    public DuelAcceptCommand(AcsCore plugin, DuelManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player target)) return true;
        DuelRequest req = manager.get(target.getUniqueId());
        if (req == null) {
            target.sendMessage(ColorUtil.color("&#FB2626No valid duel request to accept."));
            return true;
        }
        Player challenger = Bukkit.getPlayer(req.challenger);
        if (challenger == null) {
            manager.remove(target.getUniqueId());
            target.sendMessage(ColorUtil.color("&#FB2626Challenger offline"));
            return true;
        }
        manager.remove(target.getUniqueId());
        String duelWorld = plugin.getConfig().getString("duel.world","world");
        manager.start(challenger, target, duelWorld);
        return true;
    }
}