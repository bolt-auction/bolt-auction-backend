package com.neoga.boltauction.chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class RabbitMqWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${base.addr}")
    private String relayHost;
    @Value("${rabbitmq.relay.port}")
    private int relayPort;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/queue/", "/topic/")
                .setRelayHost(relayHost)
                .setRelayPort(relayPort)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
