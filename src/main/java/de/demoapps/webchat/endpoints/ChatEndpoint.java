package de.demoapps.webchat.endpoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/chat")
public class ChatEndpoint {

    private final String nickname = "TestName";

    private Session session;
    private static Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    public ChatEndpoint() {}

    @OnOpen
    public void onOpen(Session session) throws IOException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), nickname);

        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(">>> " + nickname + " has joined the chat!");
            }
        }
    }
    
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

    }

    @OnClose
    public void onClose(Session session) throws IOException {

        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(">>> " + nickname + " has left the chat!");
            }
        }

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        
        System.out.println("Error in Client " + session.getId() + ": " + throwable.getMessage());
    }
}