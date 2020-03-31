package com.neoga.platform.order.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.neoga.platform.event.AuctionEventDispatcher;
import com.neoga.platform.exception.custom.CQuickOrderException;
import com.neoga.platform.exception.custom.COrderNotFoundException;
import com.neoga.platform.item.dto.ItemDto;
import com.neoga.platform.item.service.ItemService;
import com.neoga.platform.order.dto.OrderDto;
import com.neoga.platform.order.service.OrderService;
import com.neoga.platform.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final AuthService authService;
    private final ItemService itemService;
    private final AuctionEventDispatcher auctionEventDispatcher;

    @ApiOperation(value = "낙찰자 조회", notes = "itemId로 해당 상품 낙찰자 조회" +
            "404(NotFound) - itemId에 해당하는 낙찰자 정보없음")
    @GetMapping("/{item-id}")
    public ResponseEntity insertItem(@PathVariable("item-id") Long itemId)  {
        OrderDto findOrder;
        try {
            findOrder = orderService.getOrder(itemId);
        }catch(COrderNotFoundException e){
            return ResponseEntity.notFound().build();
        }

        Resource resource = new Resource(findOrder);
        resource.add(linkTo(OrderController.class).slash(findOrder).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/item-controller/getItemUsingGET").withRel("profile"));

        return ResponseEntity.ok().body(resource);
    }

    @ApiOperation(value = "즉시 낙찰", notes = "즉시낙찰가로 해당 상품 낙찰")
    @PostMapping("/quick/{item-id}")
    public ResponseEntity quickOrder(@PathVariable("item-id") Long itemId) throws JsonProcessingException {
        Long memberId = authService.getLoginInfo().getMemberId();
        ItemDto findItem = itemService.getItem(itemId);
        if (findItem.getSeller().getId().equals(memberId)) {
            throw new CQuickOrderException("본인의 상품은 구매하실 수 없습니다.");
        } else if (findItem.isEnd()) {
            throw new CQuickOrderException("종료된 상품입니다.");
        }

        OrderDto order = orderService.quickOrder(memberId, itemId);

        auctionEventDispatcher.send(order.getMemberId(),
                findItem.getSeller().getId(),
                order.getItemId(),
                order.getPrice(),
                order.getCreateDt());

        return ResponseEntity.ok(order);
    }
}
