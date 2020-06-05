package com.neoga.platform.bid.config;

import com.neoga.platform.bid.domain.Bid;
import com.neoga.platform.bid.service.BidService;
import com.neoga.platform.event.AuctionEventDispatcher;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.service.ItemService;
import com.neoga.platform.order.domain.Orders;
import com.neoga.platform.order.service.OrderService;
import com.netflix.discovery.converters.Auto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AuctionJob implements Job {
    @Autowired
    private ItemService itemService;
    @Autowired
    private BidService bidService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuctionEventDispatcher auctionEventDispatcher;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("------------auction job start : {}", LocalDateTime.now());
        List<Item> itemFinishedList = itemService.getItemInTimeOutAuction();

        for(Item item : itemFinishedList){
            log.info("[change Item to Sell State] itemId: {}", item.getId());
            itemService.setIsEnd(item.getId(), true);

            Optional<Bid> maxBid = bidService.getMaxBidByItemId(item.getId());
            if(maxBid.isPresent()){
                log.info("[save Order] memberId: {}, itemId: {}, biId: {}",
                        maxBid.get().getMembers().getId(),
                        item.getId(),
                        maxBid.get().getPrice());

                Orders saveOrder = orderService.saveOrder(maxBid.get().getMembers().getId(),
                        item.getId(),
                        maxBid.get().getPrice());

                auctionEventDispatcher.send(saveOrder.getMembers().getId(),
                        item.getMembers().getId(),
                        saveOrder.getItem().getId(),
                        saveOrder.getPrice(),
                        saveOrder.getCreateDt());
            }
        }
    }
}
