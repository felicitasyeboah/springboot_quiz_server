package de.semesterprojekt.quiz.websocket;

import com.sun.security.auth.UserPrincipal;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        //Check if the Principal is already set
        if(request.getPrincipal() == null) {

            //Get the token from the header
            String headerString = request.getHeaders().toString();
            String token = headerString.substring(headerString.indexOf("token:\"") + 7,headerString.indexOf("token:\"") + 186);

            //Set the token as Principal
            return new UserPrincipal(token);
        } else {

            //Return already set value
            return new UserPrincipal(request.getPrincipal().getName());
        }
    }
}