package leos.dev.AcsCore.features.duel;

import java.util.UUID;

public class DuelRequest {
    public final UUID challenger;
    public final UUID target;
    public final long expireAt;

    public DuelRequest(UUID c, UUID t, long expireAt) {
        this.challenger = c;
        this.target = t;
        this.expireAt = expireAt;
    }

    public boolean expired() {
        return System.currentTimeMillis() > expireAt;
    }
}