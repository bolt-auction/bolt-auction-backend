package com.neoga.platform.order.controller;

import com.neoga.platform.order.service.OrdersService;
import com.neoga.platform.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/orders")
public class OrdersController {

    private final AuthService authService;
    private final OrdersService ordersService;

    @GetMapping("/{item-id}")
    public void quickOrders(@PathVariable(name = "item-id") Long itemId) {
        Long memberId = authService.getLoginInfo().getMemberId();

        ordersService.createOrder(memberId, itemId);
    }
}
