package ru.coolz.weatherservice.cache;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCache<K, V> {

    private final Map<K, CacheEntry<V>> cache = new HashMap<>();
    private final int ttlSeconds;

    public InMemoryCache(int ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, ttlSeconds));
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.value;
    }
}

