package de.demoapps.webchat.endpoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import de.demoapps.webchat.classes.Message;
import de.demoapps.webchat.classes.MessageDecoder;
import de.demoapps.webchat.classes.MessageEncoder;

@ApplicationScoped
@ServerEndpoint(value="/chat/{nickname}",
                decoders=MessageDecoder.class,
                encoders=MessageEncoder.class)
public class ChatEndpoint {

    private Session session;
    private static Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    public ChatEndpoint() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String nickname) throws IOException, EncodeException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), nickname);

        Message message = new Message();

        // build and broadcast join-message
        message.setFrom(nickname);
        message.setContent("<<<< has joined !");

        broadcast(message);

        // build and send userlist to the ne user
        message.setFrom("server");
        message.setSubject("userlist");

        StringBuilder content = new StringBuilder();
        users.values().forEach(user -> {
            content.append(user + ";");
        });

        message.setContent(content.toString());

        broadcast(message);

        // send MOTD to the new user
        sendMOTD(session);

        // for debugging
        debugOutput(true, nickname, session.getId(), "joined chat");
        printEndpoints();
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {

        if (message.getContent().toLowerCase().startsWith("/userlist")) {

            StringBuilder userList = new StringBuilder();

            userList.append(">>> Userlist <<<");
            users.values().forEach(user -> {
                userList.append("\n" + user);
            });

            message.setFrom("Server");
            message.setTo(session.getId());
            message.setContent(userList.toString());
            
            sendToUser(message, session);
        }
        else if (message.getContent().toLowerCase().startsWith("/help")) {

            message.setFrom("Server");
            message.setTo(session.getId());
            message.setContent("Sorry, no help yet... xP");
            
            sendToUser(message, session);
        }
        else {
            message.setFrom(users.get(session.getId()));
            broadcast(message);
        }

        // for debugging
        debugOutput(false, message.getFrom(), session.getId(), message.getContent());
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        chatEndpoints.remove(this);

        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("<<<< has left !");

        broadcast(message);

        users.remove(session.getId());

        // broadcast neu userlist
        message.setFrom("server");
        message.setSubject("userlist");

        StringBuilder content = new StringBuilder();
        users.values().forEach(user -> {
            content.append(user + ";");
        });

        message.setContent(content.toString());

        broadcast(message);

        // for debugging
        debugOutput(false, message.getFrom(), session.getId(), "left chat");
        printEndpoints();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        
        System.out.println("Error in Client " + session.getId() + ": " + throwable.getStackTrace());
    }

    public void broadcast(Message message) {

        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } 
                catch (IOException | EncodeException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void sendMOTD(Session session) {
        StringBuilder content = new StringBuilder();

        content.append("! ! !  M O T D  ! ! !\n");
        content.append(">>\n");
        content.append(">> Welcome to Simple WebChat-Demo !!\n");
        content.append(">>\n");
        content.append(">> Version: 0.1\n");
        content.append(">> Author: Holger Dörner\n");
        content.append(">>\n");
        content.append(">> Help: /help\n");
        content.append(">>\n");

        Message message = new Message();
        message.setFrom("server");
        message.setTo(session.getId());
        message.setContent(content.toString());

        sendToUser(message, session);
    }

    // send a message to a specific user only
    public void sendToUser(Message message, Session session) {

        try {
            session.getBasicRemote().sendObject(message);
        }
        catch (EncodeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // print debug output
    private void debugOutput(boolean isNew, String nickname, String uid, String content) {
        System.out.println("new user: " + isNew + "\nnick: " + nickname + "\nuid: " + uid + "\ncontent: " + content);
    }

    private void printEndpoints() {
        System.out.println("entpoints total: " + chatEndpoints.size());
    }
}