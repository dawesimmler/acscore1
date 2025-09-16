package leos.dev.AcsCore.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tasks {
    private static Plugin plugin;
    private static ExecutorService pool;

    public static void init(Plugin p) {
        plugin = p;
        pool = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()/2));
    }

    public static void shutdown() {
        if (pool != null) pool.shutdownNow();
    }

    public static BukkitTask run(Runnable r) {
        return Bukkit.getScheduler().runTask(plugin, r);
    }

    public static BukkitTask later(Runnable r, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, r, delay);
    }

    public static BukkitTask timer(Runnable r, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, r, delay, period);
    }

    public static void async(Runnable r) {
        pool.execute(r);
    }
}