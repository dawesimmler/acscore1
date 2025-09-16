package leos.dev.AcsCore.scheduler;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import leos.dev.AcsCore.util.Titles;
import org.bukkit.Bukkit;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class RestartScheduler {
    private final AcsCore plugin;
    private int taskId = -1;

    public RestartScheduler(AcsCore plugin) {
        this.plugin = plugin;
    }

    public void schedule() {
        if (!plugin.getConfig().getBoolean("restart.daily-enable", true)) return;
        String time = plugin.getConfig().getString("restart.daily-time","05:00");
        LocalTime target = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        long delayTicks = computeDelayTicks(target);
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            warnAndRestart();
            schedule();
        }, delayTicks);
    }

    private long computeDelayTicks(LocalTime target) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(zone);
        LocalDateTime targetDateTime = LocalDateTime.of(LocalDate.now(zone), target);
        if (!targetDateTime.isAfter(now)) targetDateTime = targetDateTime.plusDays(1);
        Duration d = Duration.between(now, targetDateTime);
        return d.toSeconds()*20;
    }

    private void warnAndRestart() {
        int warn = plugin.getConfig().getInt("restart.warn-seconds",10);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int c = warn;
            boolean half = false;
            @Override
            public void run() {
                if (c <= 0) {
                    for (var p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ColorUtil.color("&#FB2626Restarting now..."));
                    }
                    Bukkit.shutdown();
                    return;
                }
                if (c == warn || c == 5 || c <= 3) {
                    for (var p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ColorUtil.color("&#DEB1FBQuick Server Restart in &f" + c + "s"));
                        Titles.send(p, "&#DEB1FBRestart", "&f" + c + "s", 0, 20, 0);
                    }
                }
                c--;
            }
        },0L,20L);
    }

    public void cancel() {
        if (taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
    }
}