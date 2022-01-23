package de.semesterprojekt.quiz.config;

import de.semesterprojekt.quiz.websocket.controller.UserHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@CrossOrigin
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setHandshakeHandler(new UserHandshakeHandler());
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:4001")
                .setHandshakeHandler(new UserHandshakeHandler()).withSockJS();
        registry.addEndpoint("/websocket").setAllowedOrigins("http://192.168.178.47:4001")
                .setHandshakeHandler(new UserHandshakeHandler());
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:4001")
                .setHandshakeHandler(new UserHandshakeHandler());
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
