package com.neoga.platform.order.controller;

import com.neoga.platform.item.controller.ItemController;
import com.neoga.platform.item.dto.InsertItemDto;
import com.neoga.platform.item.dto.ItemDto;
import com.neoga.platform.item.service.ItemService;
import com.neoga.platform.memberstore.member.controller.MemberController;
import com.neoga.platform.order.domain.Orders;
import com.neoga.platform.order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @ApiOperation(value = "낙찰자 조회", notes = "itemId로 해당 상품 낙찰자 조회")
    @GetMapping("/{item-id}")
    public ResponseEntity insertItem(@PathVariable("item-id") Long itemId) throws IOException {
        Orders findOrder = orderService.getOrder(itemId).get();

        Resource resource = new Resource(findOrder);
        resource.add(linkTo(OrderController.class).slash(findOrder).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/item-controller/getItemUsingGET").withRel("profile"));

        return ResponseEntity.ok().body(resource);
    }
}
