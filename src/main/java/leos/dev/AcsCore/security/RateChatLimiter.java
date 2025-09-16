package leos.dev.AcsCore.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RateChatLimiter {
    private static final Map<UUID, Window> map = new ConcurrentHashMap<>();

    public static boolean check(UUID id, int max, long window) {
        long now = System.currentTimeMillis();
        Window w = map.computeIfAbsent(id, k -> new Window(now, 0));
        if (now - w.start >= window) {
            w.start = now;
            w.count = 0;
        }
        w.count++;
        return w.count <= max;
    }

    private static class Window {
        long start;
        int count;
        Window(long s, int c) { start = s; count = c; }
    }
}