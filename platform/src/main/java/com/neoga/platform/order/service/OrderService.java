package com.neoga.platform.order.service;

import com.neoga.platform.exception.custom.CItemNotFoundException;
import com.neoga.platform.exception.custom.COrderNotFoundException;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.repository.ItemRepository;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.order.domain.Orders;
import com.neoga.platform.order.dto.OrderDto;
import com.neoga.platform.order.repository.OrderRepository;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final EntityManager em;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;

    public Orders saveOrder(Long memberId, Long itemId, int price){
        Orders order = new Orders();
        order.setItem(em.getReference(Item.class, itemId));
        order.setMembers(em.getReference(Members.class, memberId));
        order.setPrice(price);

        return orderRepository.save(order);
    }

    public OrderDto getOrder(Long itemId) throws COrderNotFoundException{
        Orders findOrder = orderRepository.findByItem_Id(itemId)
                .orElseThrow(() -> new COrderNotFoundException("해당 itemid에 대한 낙찰기록이 없습니다."));
        return mapOrderDto(findOrder);
    }

    public OrderDto quickOrder(Long memberId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new CItemNotFoundException("해당 item이 존재하지 않습니다."));
        item.setEnd(true);
        itemRepository.save(item);
        Orders saveOrder = saveOrder(memberId, itemId, item.getQuickPrice());
        return mapOrderDto(saveOrder);
    }

    public OrderDto mapOrderDto(Orders order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderDto.setMemberId(order.getMembers().getId());
        orderDto.setItemId(order.getItem().getId());
        return orderDto;
    }
}
