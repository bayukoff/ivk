package ru.coolz.weatherservice.cache;

import java.time.Instant;

public class CacheEntry<V> {
    V value;
    Instant expiry;

    CacheEntry(V value, int ttlSeconds) {
        this.value = value;
        this.expiry = Instant.now().plusSeconds(ttlSeconds);
    }

    boolean isExpired() {
        return Instant.now().isAfter(expiry);
    }
}
