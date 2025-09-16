package leos.dev.AcsCore.teleport.request;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TpaManager {

    public enum Type { TPA, TPAHERE }

    public static class Request {
        public final UUID from;
        public final UUID to;
        public final long expireAt;
        public final Type type;
        public Request(UUID from, UUID to, long expireAt, Type type) {
            this.from = from;
            this.to = to;
            this.expireAt = expireAt;
            this.type = type;
        }
        public boolean expired() {
            return System.currentTimeMillis() > expireAt;
        }
    }

    private final Map<UUID, Request> incoming = new ConcurrentHashMap<>();

    public void put(Request r) {
        incoming.put(r.to, r);
    }

    public Request get(UUID target) {
        Request r = incoming.get(target);
        if (r != null && r.expired()) {
            incoming.remove(target);
            return null;
        }
        return r;
    }

    public Request remove(UUID target) {
        return incoming.remove(target);
    }

    public void clean() {
        long now = System.currentTimeMillis();
        incoming.entrySet().removeIf(e -> e.getValue().expireAt < now);
    }

    public Player find(UUID id) {
        return Bukkit.getPlayer(id);
    }
}