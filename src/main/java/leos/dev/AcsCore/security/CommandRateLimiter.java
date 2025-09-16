package leos.dev.AcsCore.security;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandRateLimiter {
    private final AcsCore plugin;
    private final Map<UUID, Bucket> buckets = new ConcurrentHashMap<>();

    public CommandRateLimiter(AcsCore plugin) {
        this.plugin = plugin;
    }

    public boolean allow(Player p) {
        if (p.hasPermission("acscore.bypass.ratelimit")) return true;
        int maxPerSec = plugin.getConfig().getInt("commands.perSecondLimit", 2);
        int penalSec = plugin.getConfig().getInt("commands.cooldownAfterBurstSeconds", 3);
        long now = System.currentTimeMillis();
        Bucket b = buckets.computeIfAbsent(p.getUniqueId(), k -> new Bucket());
        if (now < b.penalUntil) {
            long left = (b.penalUntil - now + 999)/1000;
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Command cooldown " + left + "s"));
            return false;
        }
        if (now - b.windowStart >= 1000) {
            b.windowStart = now;
            b.count = 0;
        }
        b.count++;
        if (b.count > maxPerSec) {
            b.penalUntil = now + penalSec*1000L;
            p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Too many commands"));
            return false;
        }
        return true;
    }

    private static class Bucket {
        long windowStart = System.currentTimeMillis();
        int count = 0;
        long penalUntil = 0;
    }
}