package com.neoga.platform.order.service;

import com.neoga.platform.chat.domain.ChatRoom;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.repository.ItemRepository;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.order.domain.Orders;
import com.neoga.platform.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final EntityManager em;

    public void saveOrder(Long memberId, Long itemId, int price){
        Orders order = new Orders();
        order.setItem(em.getReference(Item.class, itemId));
        order.setMembers(em.getReference(Members.class, memberId));
        order.setPrice(price);

        orderRepository.save(order);
    }
}
