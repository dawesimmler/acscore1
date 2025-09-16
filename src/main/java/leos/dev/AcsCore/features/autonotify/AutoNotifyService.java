package leos.dev.AcsCore.features.autonotify;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import leos.dev.AcsCore.util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class AutoNotifyService {
    private final AcsCore plugin;
    private BukkitTask task;

    public AutoNotifyService(AcsCore plugin) {
        this.plugin = plugin;
    }

    public void start() {
        long interval = plugin.getConfig().getLong("chat.discord-broadcast-seconds",600);
        if (interval <= 0) return;
        task = Tasks.timer(() -> {
            List<String> lines = plugin.getConfig().getStringList("notify.discord-lines");
            for (Player p : Bukkit.getOnlinePlayers()) {
                for (String l : lines) p.sendMessage(ColorUtil.color(l));
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ColorUtil.color("&#DEB1FBUse /bot to train!"));
            }
        }, interval*20L, interval*20L);
    }

    public void stop() {
        if (task != null) task.cancel();
    }
}