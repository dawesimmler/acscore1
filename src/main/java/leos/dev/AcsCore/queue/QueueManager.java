package leos.dev.AcsCore.queue;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class QueueManager {
    private final Set<UUID> queue = new LinkedHashSet<>();

    public boolean join(UUID u) {
        return queue.add(u);
    }

    public boolean leave(UUID u) {
        return queue.remove(u);
    }

    public boolean contains(UUID u) {
        return queue.contains(u);
    }

    public int size() {
        return queue.size();
    }
}