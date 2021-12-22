package de.semesterprojekt.quiz.websocket;

import com.sun.security.auth.UserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        //Create Session IDs for the users
        String sessionId = UUID.randomUUID().toString();
        System.out.println("User connected with websocket-sessionId: " + sessionId);

        return new UserPrincipal(sessionId);
    }
}
