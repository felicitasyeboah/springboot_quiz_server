package de.semesterprojekt.quiz.websocket.controller;

import com.sun.security.auth.UserPrincipal;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    UserRepository userRepository;

    /**
     * The method sets the session id of the calling user to an uuid to send messages to specific users
     * @param request
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        //Add an uuid as principal
        return new UserPrincipal(UUID.randomUUID().toString());
    }
}