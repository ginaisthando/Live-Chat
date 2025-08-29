package com.brightfuture.chat.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.servlet.http.HttpSession;

@ServerEndpoint(value = "/chat", configurator = HttpSessionConfigurator.class)
public class ChatEndpoint {
    
    // Set to store all active WebSocket sessions
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // Get HTTP session from WebSocket session
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        
        if (httpSession != null && httpSession.getAttribute("username") != null) {
            String username = (String) httpSession.getAttribute("username");
            session.getUserProperties().put("username", username);
            sessions.add(session);
            
            // Notify all users that someone joined
            String joinMessage = "[System] " + username + " joined the chat";
            broadcastMessage(joinMessage);
            
            System.out.println("WebSocket opened for user: " + username);
        } else {
            // Close connection if user is not authenticated
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        String username = (String) session.getUserProperties().get("username");
        
        if (username != null && message != null && !message.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String formattedMessage = "[" + timestamp + "] " + username + ": " + message.trim();
            
            // Broadcast message to all connected clients
            broadcastMessage(formattedMessage);
            
            System.out.println("Message from " + username + ": " + message);
        }
    }
    
    @OnClose
    public void onClose(Session session) {
        String username = (String) session.getUserProperties().get("username");
        sessions.remove(session);
        
        if (username != null) {
            // Notify all users that someone left
            String leaveMessage = "[System] " + username + " left the chat";
            broadcastMessage(leaveMessage);
            
            System.out.println("WebSocket closed for user: " + username);
        }
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = (String) session.getUserProperties().get("username");
        System.err.println("WebSocket error for user " + username + ": " + throwable.getMessage());
        throwable.printStackTrace();
        
        sessions.remove(session);
    }
    
    // Broadcast message to all connected clients
    private void broadcastMessage(String message) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        System.err.println("Error broadcasting message: " + e.getMessage());
                        sessions.remove(session);
                    }
                }
            }
        }
    }
    
    // Get count of active users
    public static int getActiveUserCount() {
        return sessions.size();
    }
}
