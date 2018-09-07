package de.demoapps.webchat.endpoints;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Scanner;
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

/**
 * ChatEndpoint registers a WebSocket-server.
 *  
 * @author Holger Dörner
 */
@ApplicationScoped
@ServerEndpoint(value="/chat/{nickname}",
                decoders=MessageDecoder.class,
                encoders=MessageEncoder.class)
public class ChatEndpoint {

    private Session session;
    private static Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    public ChatEndpoint() {}

    /**
     * handels new sessions
     * 
     * @param session
     * @param nickname
     * @throws IOException
     * @throws EncodeException
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String nickname) throws IOException, EncodeException {

        this.session = session;
        chatEndpoints.add(this);
        users.put(session.getId(), nickname);

        // send MOTD to the new user
        sendMOTD(session);

        Message message = new Message();

        // build and broadcast join-message
        message.setFrom(nickname);
        message.setContent("<<<< has joined !");

        broadcastMessage(message);

        // build and send userlist to the ne user
        message.setFrom("server");
        message.setSubject("userlist");

        StringBuilder content = new StringBuilder();
        users.values().forEach(user -> {
            content.append(user + ";");
        });

        message.setContent(content.toString());

        broadcastMessage(message);
    }

    /**
     * filters incomming messages and decides how to handel them.
     * 
     * @param session
     * @param message
     * @throws IOException
     * @throws EncodeException
     */
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {

        if (message.getContent().toLowerCase().startsWith("/userlist")) {

            sendUserlist(session);
        }
        else if (message.getContent().toLowerCase().startsWith("/motd")) {

            sendMOTD(session);
        }
        else if (message.getContent().toLowerCase().startsWith("/help")) {

            sendHelp(session);
        }
        else {
            message.setFrom(users.get(session.getId()));
            broadcastMessage(message);
        }
    }

    /**
     * brodcasts a message and removes the session/endpoint when a session is closed.
     * 
     * @param session
     * @throws IOException
     * @throws EncodeException
     */
    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        chatEndpoints.remove(this);

        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setContent("<<<< has left !");

        broadcastMessage(message);

        users.remove(session.getId());

        session.close();

        // broadcast neu userlist
        message.setFrom("server");
        message.setSubject("userlist");

        StringBuilder content = new StringBuilder();
        users.values().forEach(user -> {
            content.append(user + ";");
        });

        message.setContent(content.toString());

        broadcastMessage(message);
    }

    /**
     * error handling and logging
     * 
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        
        System.out.println("Error in Client " + session.getId() + ": " + throwable.getStackTrace());
    }

    /**
     * broadcast messages to all connectet sessions
     * 
     * @param message
     */
    public void broadcastMessage(Message message) {

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

    /**
     * send a message to a specific user only
     * 
     * @param message
     * @param session
     */
    public void directMessage(Message message, Session session) {

        try {
            session.getBasicRemote().sendObject(message);
        }
        catch (EncodeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * sends actual userlist to client
     * 
     * @param session
     */
    public void sendUserlist(Session session) {

        StringBuilder userList = new StringBuilder();

        userList.append(">>> Userlist <<<");
        users.values().forEach(user -> {
            userList.append("\n" + user);
        });

        Message message = new Message();

        message.setFrom("Server");
        message.setTo(session.getId());
        message.setContent(userList.toString());
        
        directMessage(message, session);
    }

    /**
     * reads the motd.txt from the document-root and sends the message to the client.
     * 
     * @param session
     * @throws UnsupportedEncodingException
     */
    private void sendMOTD(Session session) throws UnsupportedEncodingException {

        StringBuilder content = new StringBuilder();

        // get path of webcontent-folder
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        String pathArr[] = fullPath.split("/WEB-INF/classes/");
        fullPath = pathArr[0];

        // to read a file from webcontent
        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(new File(fullPath).getPath() + File.separatorChar + "motd.txt"));

            content.append("- - - MESSAGE OF THE DAY - - -\n\n");

            while (scanner.hasNext()) {
                content.append(scanner.nextLine() + "\n");
            }

            Message message = new Message();
            message.setFrom("server");
            message.setTo(session.getId());
            message.setContent(content.toString());

            directMessage(message, session);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            scanner.close();
        }
        
    }

    /**
     * sends help-message to the client
     * 
     * @param session
     */
    public void sendHelp(Session session) {

        Message message = new Message();

        message.setFrom("Server");
        message.setTo(session.getId());
        message.setContent("Sorry, no help yet... xP");
        
        directMessage(message, session);
    }
}