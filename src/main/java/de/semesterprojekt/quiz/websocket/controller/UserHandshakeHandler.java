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
     * The method sets the JWT of the calling user as a session-id to send messages to specific users
     * @param request
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String uuid = UUID.randomUUID().toString();
        System.out.println("UUID: " + uuid);
        return new UserPrincipal(uuid);

           /*
        //Check if the Principal is already set
        if(request.getPrincipal() == null) {

            //Get the token from the header
            String headerString = request.getHeaders().toString();

            //Create a substring beginning with the token
            headerString = headerString.substring(headerString.indexOf("token:\"") + 7);

            //find the last character of the string
            headerString = headerString.substring(0, headerString.indexOf("\""));

            //Return the token
            return new UserPrincipal(headerString);
        } else {

            //Return already set value
            return new UserPrincipal(request.getPrincipal().getName());
        }
        */

    }
}