package ru.coolz.fileshare.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.coolz.fileshare.entity.FileRecord;

public class FileService {

    private final ConcurrentMap<String, FileRecord> files = new ConcurrentHashMap<>();
    private final Path uploadDir = Paths.get("uploads");
    private final long EXPIRATION = 30L * 24 * 60 * 60; // 30 дней в секундах

    public FileService() throws IOException {
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
    }

    public String saveFile(String originalName, Path tempFile) throws IOException {
        String id = UUID.randomUUID().toString();
        Path target = uploadDir.resolve(id + "_" + originalName);
        Files.move(tempFile, target, StandardCopyOption.REPLACE_EXISTING);

        files.putIfAbsent(id, new FileRecord(id, originalName, target.toString()));
        return "/download?id=" + id;
    }

    public FileRecord getFile(String id) {
        FileRecord record = files.get(id);
        if (record != null) {
            record.updateAccess();
        }
        return record;
    }

    public void cleanup() {
        Instant now = Instant.now();

        List<String> expiredIds = files.entrySet().stream()
            .filter(e -> now.getEpochSecond() - e.getValue().getLastAccessed().getEpochSecond() > EXPIRATION)
            .map(Map.Entry::getKey)
            .toList();

        for (String id : expiredIds) {
            FileRecord record = files.remove(id);
            if (record != null) {
                try {
                    Files.deleteIfExists(Paths.get(record.getPath()));
                } catch (IOException ignored) {}
            }
        }
    }
}

