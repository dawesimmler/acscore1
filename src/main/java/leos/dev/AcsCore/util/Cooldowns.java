package leos.dev.AcsCore.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cooldowns {
    private final Map<UUID, Long> map = new ConcurrentHashMap<>();

    public boolean ready(UUID id, long cooldownMillis) {
        long now = System.currentTimeMillis();
        long last = map.getOrDefault(id, 0L);
        if (now - last >= cooldownMillis) {
            map.put(id, now);
            return true;
        }
        return false;
    }

    public long left(UUID id, long cooldownMillis) {
        long now = System.currentTimeMillis();
        long last = map.getOrDefault(id, 0L);
        long diff = now - last;
        long left = cooldownMillis - diff;
        return Math.max(left, 0);
    }

    public void clear(UUID id) {
        map.remove(id);
    }
}