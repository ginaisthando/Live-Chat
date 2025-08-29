package com.brightfuture.chat.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // Get HTTP session from the handshake request
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        if (httpSession != null) {
            // Store HTTP session in WebSocket endpoint configuration
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }
    }
}
