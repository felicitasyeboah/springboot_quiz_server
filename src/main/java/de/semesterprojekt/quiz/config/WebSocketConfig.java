package de.semesterprojekt.quiz.config;

import de.semesterprojekt.quiz.websocket.controller.UserHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * The class configures the websocket settings
 */
@CrossOrigin
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        //Endpoint for the Android client
        registry.addEndpoint("/websocket")
                .setHandshakeHandler(new UserHandshakeHandler());

        //Endpoint for the Vue client
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:4000")
                .setHandshakeHandler(new UserHandshakeHandler()).withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
