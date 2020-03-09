package com.neoga.boltauction.chat.controller;

import com.google.gson.Gson;
import com.neoga.boltauction.chat.domain.ChatMessage;
import com.neoga.boltauction.chat.dto.SendMessageDto;
import com.neoga.boltauction.chat.service.ChatMessageService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@RequiredArgsConstructor
@RequestMapping
@RestController
@Slf4j
public class ChatController {
    private final ChatMessageService chatMessageService;

    @ApiOperation(value = "과거 메시지 목록 조회", notes = "자신의 메시지 목록 반환, 파라미터로 페이징 처리가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,dataType = "integer", paramType = "query",
                    value = "페이지 번호 (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "페이지당 채팅 수", defaultValue = "6"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "property(,asc|desc) 기본 내림차순")
    })
    @GetMapping("/api/chat/message")
    public ResponseEntity<PagedResources<Resource<ChatMessage>>> listOldMessages(@RequestParam Long chatRoomId,
                                                                                 @ApiIgnore Pageable pageable,
                                                                                 @ApiIgnore PagedResourcesAssembler<ChatMessage> assembler){
        Page<ChatMessage> messageList = chatMessageService.findMessageByChatRoom(chatRoomId, pageable);

        PagedResources<Resource<ChatMessage>> resources = assembler.toResource(messageList, i -> new Resource<>(i));
        resources.add(new Link("/swagger-ui.html#/chat-room-controller/").withRel("profile"));

        return ResponseEntity.ok(resources);
    }

    @MessageMapping("/chat.send.message.{chatRoomId}")
    public void sendMessage(@Payload SendMessageDto sendMessageDto, @DestinationVariable Long chatRoomId){
        log.info("recevied message : + " + sendMessageDto.getContent());
        chatMessageService.sendMessage(chatRoomId, sendMessageDto);
    }
}
