package com.neoga.platform.order.service;

import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.repository.ItemRepository;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.member.repository.MemberRepository;
import com.neoga.platform.order.domain.Orders;
import com.neoga.platform.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Override
    public void createOrder(Long memberId, Long itemId) {
        Members findMember = memberRepository.getOne(memberId);
        Item findItem = itemRepository.getOne(itemId);

        findItem.setSell(true);

        Orders orders = new Orders();
        orders.setMembers(findMember);
        orders.setItem(findItem);

        ordersRepository.save(orders);
        itemRepository.save(findItem);
    }
}
