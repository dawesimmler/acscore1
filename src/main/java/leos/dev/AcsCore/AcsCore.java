package leos.dev.AcsCore;

import leos.dev.AcsCore.bootstrap.CoreBootstrap;
import leos.dev.AcsCore.config.ConfigService;
import leos.dev.AcsCore.features.announcements.AnnouncementService;
import leos.dev.AcsCore.features.autonotify.AutoNotifyService;
import leos.dev.AcsCore.features.deathmessages.DeathMessageListener;
import leos.dev.AcsCore.features.doublejump.DoubleJumpListener;
import leos.dev.AcsCore.listeners.CoreJoinQuitListener;
import leos.dev.AcsCore.listeners.DeathRecordListener;
import leos.dev.AcsCore.listeners.InvseeListener;
import leos.dev.AcsCore.listeners.PlayerSnapshotListener;
import leos.dev.AcsCore.scheduler.RestartScheduler;
import leos.dev.AcsCore.security.ChatListener;
import leos.dev.AcsCore.security.CommandRateLimiter;
import leos.dev.AcsCore.security.CommandSpectatorBlocker;
import leos.dev.AcsCore.security.SpawnProtection;
import leos.dev.AcsCore.util.Tasks;
import leos.dev.AcsCore.world.DeathLocationTracker;
import leos.dev.AcsCore.world.SpawnManager;
import leos.dev.AcsCore.gameplay.rules.RulesListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AcsCore extends JavaPlugin {
    private static AcsCore instance;
    private ConfigService configService;
    private SpawnManager spawnManager;
    private AnnouncementService announcementService;
    private AutoNotifyService autoNotifyService;
    private RestartScheduler restartScheduler;
    private DeathLocationTracker deathLocationTracker;
    private CommandRateLimiter commandRateLimiter;
    private SpawnProtection spawnProtection;

    public static AcsCore get() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        this.configService = new ConfigService(this);
        // Flat world manager completely removed
    }

    @Override
    public void onEnable() {
        Tasks.init(this);
        this.spawnManager = new SpawnManager(this);
        this.deathLocationTracker = new DeathLocationTracker();
        this.commandRateLimiter = new CommandRateLimiter(this);
        this.spawnProtection = new SpawnProtection(this);
        this.announcementService = new AnnouncementService(this);
        this.autoNotifyService = new AutoNotifyService(this);
        this.restartScheduler = new RestartScheduler(this);

        new CoreBootstrap(this).registerEarly();
        registerListeners();

        // No flat world creation - use existing worlds for RTP
        getLogger().info("AcsCore enabled - using existing worlds for RTP functionality");

        restartScheduler.schedule();
        autoNotifyService.start();
        announcementService.init();
    }

    private void registerListeners() {
        var pm = Bukkit.getPluginManager();
        pm.registerEvents(new CoreJoinQuitListener(this), this);
        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new DoubleJumpListener(this), this);
        pm.registerEvents(spawnProtection, this);
        pm.registerEvents(new CommandSpectatorBlocker(this), this);
        pm.registerEvents(new DeathRecordListener(this), this);
        pm.registerEvents(new InvseeListener(), this);
        pm.registerEvents(new PlayerSnapshotListener(), this);
        pm.registerEvents(new RulesListener(), this);
        pm.registerEvents(new DeathMessageListener(this), this);
    }

    @Override
    public void onDisable() {
        autoNotifyService.stop();
        announcementService.shutdown();
        restartScheduler.cancel();
        Tasks.shutdown();
    }

    public ConfigService configService() {
        return configService;
    }

    public SpawnManager spawnManager() {
        return spawnManager;
    }

    public AnnouncementService announcementService() {
        return announcementService;
    }

    public AutoNotifyService autoNotifyService() {
        return autoNotifyService;
    }

    public RestartScheduler restartScheduler() {
        return restartScheduler;
    }

    public DeathLocationTracker deathLocationTracker() {
        return deathLocationTracker;
    }

    public CommandRateLimiter commandRateLimiter() {
        return commandRateLimiter;
    }

    public SpawnProtection spawnProtection() {
        return spawnProtection;
    }
}