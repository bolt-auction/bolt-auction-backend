package com.neoga.boltauction.chat.controller;


import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.dto.CreateRoomRequestDto;
import com.neoga.boltauction.chat.service.ChatRoomService;
import com.neoga.boltauction.item.controller.ItemController;
import com.neoga.boltauction.item.dto.ItemDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat/room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @ApiOperation(value = "채팅방 목록 조회", notes = "자신의 채팅방 목록 반환, 파라미터로 페이징 처리가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" , value = "페이지 번호 (0..N)", dataType = "integer", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "페이지당 채팅창 수", dataType = "integer", paramType = "query", defaultValue = "6"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, paramType = "query",value = "property(,asc|desc) 기본 내림차순")
    })
    @GetMapping
    public ResponseEntity<PagedResources<Resource<ChatRoom>>> findRoomInMember(@ApiIgnore Pageable pageable,
                                           @ApiIgnore PagedResourcesAssembler<ChatRoom> assembler) {
        Page<ChatRoom> roomJoin = chatRoomService.findChatRoomList(pageable);

        PagedResources<Resource<ChatRoom>> resources = assembler.toResource(roomJoin, i -> new Resource<>(i));
        resources.add(new Link("/swagger-ui.html#/chat-room-controller/findRoomInMemberUsingGET").withRel("profile"));

        return ResponseEntity.ok(resources);
    }

    @ApiOperation(value = "1:1 채팅방 생성", notes = "대화 상대방 memberId와 거래상품 id, 채팅방 이름으로 대화창 생성")
    @PostMapping
    public ResponseEntity createRoom(@RequestBody CreateRoomRequestDto createRoomDto) {
        ChatRoom newRoom = chatRoomService.createChatRoom(createRoomDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }
}
