package com.neoga.boltauction.item.controller;

import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.item.dto.InsertItemDto;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.dto.UpdateItemDto;
import com.neoga.boltauction.item.service.ItemService;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.service.MemberService;
import com.neoga.boltauction.memberstore.store.controller.StoreController;
import com.neoga.boltauction.security.service.AuthService;
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
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/item")
public class ItemController {

    private final ItemService itemService;
    private final AuthService authService;
    private final MemberService memberService;

    @ApiOperation(value = "카테고리별 상품조회", notes = "sort=creatDt,ASC 등으로 정렬방식 선택 가능")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,dataType = "integer", paramType = "query",
                    value = "페이지 번호 (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "페이지의 아이템 수", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "인기순 : bidCount,asc\n" +
                            "최신순 : createDt,asc\n" +
                            "낮은가격순 : currentPrice,asc\n" +
                            "높은가격순 : currentPrice,desc\n")
    })
    @GetMapping("/category/{category-id}")
    public ResponseEntity getItems(@PathVariable(name = "category-id") Long categoryId, @ApiIgnore Pageable pageable,
                                   @ApiIgnore PagedResourcesAssembler<ItemDto> assembler) {
        // 권한체크 추가

        Page<ItemDto> itemDtoPage = itemService.getItems(categoryId, pageable);

        PagedResources<Resource<ItemDto>> resources = assembler.toResource(itemDtoPage, i -> new Resource<>(i));
        resources.forEach(resource -> resource.add(linkTo(methodOn(ItemController.class).getItem(resource.getContent().getItemId())).withRel("item-detail")));
        resources.add(new Link("/swagger-ui.html#/item-controller/getItemsUsingGET").withRel("profile"));

        return ResponseEntity.ok(resources);
    }

    @ApiOperation(value = "상품등록(로그인 필요)", notes = "swagger 에서 이미지 등록 불가능")
    @PostMapping
    public ResponseEntity insertItem(@Valid InsertItemDto insertItemDto,
                                             MultipartFile... images) throws IOException {
        // get memberId
        Long memberId = authService.getLoginInfo().getMemberId();

        // save item
        ItemDto saveItemDto = itemService.saveItem(insertItemDto, memberId, images);

        ControllerLinkBuilder selfLinkBuilder =linkTo(ItemController.class).slash(saveItemDto.getItemId());
        URI createdUri = selfLinkBuilder.toUri();
        Resource resource = new Resource(saveItemDto);

        resource.add(linkTo(methodOn(ItemController.class).getItem(saveItemDto.getItemId())).withRel("item-detail"));
        resource.add(linkTo(ItemController.class).withRel("query-events"));
        resource.add(selfLinkBuilder.withRel("update-event"));
        resource.add(new Link("/swagger-ui.html#/item-controller/insertItemUsingPOST").withRel("profile"));

        return ResponseEntity.created(createdUri).body(resource);
    }

    @ApiOperation(value = "상품조회", notes = "특정상품 조회")
    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable(name = "item-id") Long id) {
        // 권한 체크

        ItemDto findItem = itemService.getItem(id);

        Resource resource = new Resource(findItem);
        resource.add(linkTo(ItemController.class).slash(findItem.getItemId()).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/item-controller/getItemUsingGET").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "상품삭제", notes = "반환 메세지 미정")
    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item-id") Long id) {
        //권한 체크 해당 사용자인지 체크

        // delete item entity
        try {
            itemService.deleteItem(id);
        } catch (CItemNotFoundException e) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "상품수정(로그인 필요)", notes = "")
    @PutMapping("/{item-id}")
    public ResponseEntity updateItem(@PathVariable(name = "item-id") Long id,
                                     @Valid UpdateItemDto updateItemDto,
                                     MultipartFile... images) throws IOException {

        Long memberId = authService.getLoginInfo().getMemberId();
        Members findMember = memberService.findMemberById(memberId);
        ItemDto findItem = itemService.getItem(id);

        if (findItem.getSellerId() != findMember.getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ItemDto itemDto = itemService.updateItem(id, updateItemDto, memberId, images);

        Resource resource = new Resource(itemDto);
        resource.add(linkTo(ItemController.class).slash(itemDto.getItemId()).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/item-controller/updateItemUsingPUT").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "상품검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filter", value = "검색조건 ex)name", dataType = "string"),
            @ApiImplicitParam(name = "keyword", value = "검색 키워드", dataType = "string"),
            @ApiImplicitParam(name = "page" ,dataType = "integer", paramType = "query",
                    value = "페이지 번호 (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "페이지의 아이템 수", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "인기순 : bidCount,asc\n" +
                            "최신순 : createDt,asc\n" +
                            "낮은가격순 : currentPrice,asc\n" +
                            "높은가격순 : currentPrice,desc\n")
    })
    @GetMapping
    public ResponseEntity<PagedResources<Resource<ItemDto>>> searchItem(@RequestParam String filter,@RequestParam String keyword, @ApiIgnore Pageable pageable,
                                                                        @ApiIgnore PagedResourcesAssembler<ItemDto> assembler) {
        Page<ItemDto> itemDtoPage = itemService.searchItem(filter, keyword, pageable);

        PagedResources<Resource<ItemDto>> resources = assembler.toResource(itemDtoPage, i -> new Resource<>(i));
        resources.forEach(resource -> resource.add(linkTo(methodOn(ItemController.class).getItem(resource.getContent().getItemId())).withRel("item-detail")));
        resources.add(new Link("/swagger-ui.html#/item-controller/searchItemUsingGET").withRel("profile"));

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/store/{member-id}")
    public ResponseEntity getStoreItems(@PathVariable(name = "member-id") Long memberId){
        List<ItemDto> itemDtoList = itemService.getItemsByMemberId(memberId);

        List<Resource> resourceList = itemDtoList.stream().map(itemDto -> {
            Resource resource = new Resource(itemDto);
            resource.add(linkTo(ItemController.class).slash(itemDto.getItemId()).withSelfRel());
            return resource;
        }).collect(Collectors.toList());

        Resources resources = new Resources(resourceList);
        resources.add(linkTo(StoreController.class).slash("store/" + memberId).withSelfRel());
        resources.add(new Link("/swagger-ui.html#/item-controller/getStoreItemsUsingGET").withRel("profile"));

        return ResponseEntity.ok(resources);
    }
}
