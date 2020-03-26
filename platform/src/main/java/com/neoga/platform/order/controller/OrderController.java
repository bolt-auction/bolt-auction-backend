package com.neoga.platform.order.controller;


import com.neoga.platform.exception.custom.COrderNotFoundException;
import com.neoga.platform.order.dto.OrderDto;
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

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

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
}
