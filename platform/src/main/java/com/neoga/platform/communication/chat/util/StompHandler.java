package com.neoga.platform.communication.chat.util;

/*
@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenService jwtTokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel chatnnel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            log.info("CONNECT {}", jwtToken);
            jwtTokenService.validateToken(jwtToken);
        }
    }
}
*/
