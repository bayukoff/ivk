package ru.coolz.fileshare.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AuthService{

    private final Map<String, String> users = new HashMap<>();
    private final ConcurrentMap<String, String> sessions = new ConcurrentHashMap<>();

    public AuthService() {
        users.put("admin", "1234");
        users.put("user", "password");
    }

    public boolean authenticate(String username, String password) {
        return password.equals(users.get(username));
    }

    public String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, username);
        return sessionId;
    }

    public String getUsernameBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
