package de.demoapps.webchat.classes;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

@ApplicationScoped
public class SessionHandler {
    
    private final Set<Session> sessions = new HashSet<>();
    private final Set<User> users = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
}