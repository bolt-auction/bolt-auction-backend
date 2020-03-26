package com.neoga.platform.order.service;

import com.neoga.platform.exception.custom.CItemNotFoundException;
import com.neoga.platform.exception.custom.COrderNotFoundException;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.dto.ItemDto;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.order.domain.Orders;
import com.neoga.platform.order.dto.OrderDto;
import com.neoga.platform.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final EntityManager em;
    private final ModelMapper modelMapper;

    public void saveOrder(Long memberId, Long itemId, int price){
        Orders order = new Orders();
        order.setItem(em.getReference(Item.class, itemId));
        order.setMembers(em.getReference(Members.class, memberId));
        order.setPrice(price);

        orderRepository.save(order);
    }

    public OrderDto getOrder(Long itemId) throws COrderNotFoundException{
        Orders findOrder = orderRepository.findByItem_Id(itemId)
                .orElseThrow(() -> new COrderNotFoundException("해당 itemid에 대한 낙찰기록이 없습니다."));
        return mapOrderDto(findOrder);
    }

    public OrderDto mapOrderDto(Orders order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setMemberId(order.getMembers().getId());
        orderDto.setItemId(order.getItem().getId());
        return orderDto;
    }
}
