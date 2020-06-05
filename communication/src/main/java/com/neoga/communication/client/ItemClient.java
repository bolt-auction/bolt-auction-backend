package com.neoga.communication.client;

import com.neoga.communication.chat.dto.ItemDto;
import com.neoga.communication.chat.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class ItemClient {
    private final RestTemplate restTemplate;
    @Value("${platformHost}")
    private String platformHost;

    public ItemDto retrieveItemById(final Long itemId){
        return restTemplate.getForObject(
                platformHost + "/api/item/" + itemId,
                ItemDto.class);
    }
}

