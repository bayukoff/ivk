package ru.coolz.fileshare.entity;

import java.time.Instant;

public class FileRecord {
    private final String id;
    private final String originalName;
    private final String path;
    private Instant lastAccessed;

    public FileRecord(String id, String originalName, String path) {
        this.id = id;
        this.originalName = originalName;
        this.path = path;
        this.lastAccessed = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getPath() {
        return path;
    }

    public Instant getLastAccessed() {
        return lastAccessed;
    }

    public void updateAccess() {
        lastAccessed = Instant.now();
    }
}
