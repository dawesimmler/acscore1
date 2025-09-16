package leos.dev.AcsCore.commands.core;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.commands.flight.FlyCommand;
import leos.dev.AcsCore.commands.flight.FlySpeedCommand;
import leos.dev.AcsCore.commands.gameplay.*;
import leos.dev.AcsCore.commands.gamemode.GamemodeCreativeCommand;
import leos.dev.AcsCore.commands.gamemode.GamemodeSpectatorCommand;
import leos.dev.AcsCore.commands.gamemode.GamemodeSurvivalCommand;
import leos.dev.AcsCore.commands.inventory.InvseeCommand;
import leos.dev.AcsCore.commands.messaging.MsgCommand;
import leos.dev.AcsCore.commands.messaging.ReplyCommand;
import leos.dev.AcsCore.commands.queue.LeaveQueueCommand;
import leos.dev.AcsCore.commands.queue.QueueCommand;
import leos.dev.AcsCore.commands.spawn.SetSpawnCommand;
import leos.dev.AcsCore.commands.spawn.SpawnCommand;
import leos.dev.AcsCore.commands.teleport.back.BackCommand;
import leos.dev.AcsCore.commands.teleport.core.TpCommand;
import leos.dev.AcsCore.commands.teleport.core.TphereCommand;
import leos.dev.AcsCore.commands.teleport.request.*;
import leos.dev.AcsCore.features.duel.*;
import leos.dev.AcsCore.features.tournament.TournamentCommand;
import leos.dev.AcsCore.messaging.PrivateMessageManager;
import leos.dev.AcsCore.queue.QueueManager;
import leos.dev.AcsCore.teleport.request.TpaManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistrar {
    private static TpaManager tpaManager;
    private static PrivateMessageManager pmManager;
    private static DuelManager duelManager;
    private static QueueManager queueManager;

    public static void register(AcsCore plugin) {
        if (tpaManager == null) tpaManager = new TpaManager();
        if (pmManager == null) pmManager = new PrivateMessageManager();
        if (duelManager == null) duelManager = new DuelManager(plugin);
        if (queueManager == null) queueManager = new QueueManager();

        reg(plugin,"announce", new AnnounceCommand(plugin));
        reg(plugin,"clear", new ClearCommand(plugin));
        reg(plugin,"dc", new DiscordCommand(plugin));
        reg(plugin,"discord", new DiscordCommand(plugin));
        reg(plugin,"ping", new PingCommand(plugin));
        reg(plugin,"heal", new HealCommand(plugin));
        reg(plugin,"acscore", new AcscoreRootCommand(plugin));
        reg(plugin,"acscorereload", new ReloadCommand(plugin));
        reg(plugin,"tpa", new TpaCommand(plugin, tpaManager));
        reg(plugin,"tpahere", new TpahereCommand(plugin, tpaManager));
        reg(plugin,"tpaccept", new TpAcceptCommand(plugin, tpaManager));
        reg(plugin,"tpdeny", new TpDenyCommand(plugin, tpaManager));
        reg(plugin,"back", new BackCommand(plugin));
        reg(plugin,"fly", new FlyCommand(plugin));
        reg(plugin,"flyspeed", new FlySpeedCommand(plugin));
        reg(plugin,"gmc", new GamemodeCreativeCommand());
        reg(plugin,"gms", new GamemodeSurvivalCommand());
        reg(plugin,"gmsp", new GamemodeSpectatorCommand());
        reg(plugin,"invsee", new InvseeCommand(plugin));
        reg(plugin,"rules", new RulesCommand());
        reg(plugin,"mobclear", new MobclearCommand(plugin));
        reg(plugin,"setspawn", new SetSpawnCommand(plugin));
        reg(plugin,"spawn", new SpawnCommand(plugin));
        reg(plugin,"msg", new MsgCommand(plugin, pmManager));
        reg(plugin,"r", new ReplyCommand(plugin, pmManager));
        reg(plugin,"duel", new DuelCommand(plugin, duelManager));
        reg(plugin,"duelaccept", new DuelAcceptCommand(plugin, duelManager));
        reg(plugin,"dueldeny", new DuelDenyCommand(plugin, duelManager));
        TournamentCommand tournament = new TournamentCommand(plugin);
        reg(plugin,"tournament", tournament);

        // Simple queue system (not connected to RTP - use AllayRtp plugin for RTP functionality)
        reg(plugin,"queue", new QueueCommand(plugin, queueManager));
        reg(plugin,"leave", new LeaveQueueCommand(plugin, queueManager));
    }

    private static void reg(JavaPlugin plugin, String name, Object executor) {
        PluginCommand c = plugin.getCommand(name);
        if (c != null && executor instanceof org.bukkit.command.CommandExecutor ce) {
            c.setExecutor(ce);
            if (executor instanceof org.bukkit.command.TabExecutor te) c.setTabCompleter(te);
        }
    }
}