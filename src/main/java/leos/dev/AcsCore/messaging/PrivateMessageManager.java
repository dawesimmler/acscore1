package leos.dev.AcsCore.messaging;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PrivateMessageManager {
    private final Map<UUID, UUID> last = new ConcurrentHashMap<>();

    public void link(UUID a, UUID b) {
        last.put(a,b);
        last.put(b,a);
    }

    public UUID last(UUID a) {
        return last.get(a);
    }
}