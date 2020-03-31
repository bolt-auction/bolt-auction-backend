package com.neoga.communication.config;


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
    @Value("${rabbitmq.uid}")
    private String rabbitUid;
    @Value("${rabbitmq.passwd}")
    private String rabbitPasswd;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/queue/", "/topic/")
                .setRelayHost(relayHost)
                .setRelayPort(relayPort)
                .setUserDestinationBroadcast("/topic/unresolved.user.dest")
                .setUserRegistryBroadcast("/topic/registry.broadcast")
                .setClientLogin(rabbitUid)
                .setClientPasscode(rabbitPasswd)
                .setSystemLogin(rabbitUid)
                .setSystemPasscode(rabbitPasswd);
        registry.setApplicationDestinationPrefixes("/app");
    }



}
