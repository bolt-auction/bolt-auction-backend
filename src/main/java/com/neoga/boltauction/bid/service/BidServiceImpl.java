package com.neoga.boltauction.bid.service;

import com.neoga.boltauction.bid.domain.Bid;
import com.neoga.boltauction.bid.dto.BidDto;
import com.neoga.boltauction.bid.repository.BidRepository;
import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity getBidList(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.noContent().build();
        }

        Item findItem = optionalItem.get();

        List<Bid> findBidList = bidRepository.findAllByItemOrderByPriceAsc(findItem);
        List<BidDto> bidDtoList = findBidList.stream().map(bid -> modelMapper.map(bid, BidDto.class)).collect(Collectors.toList());

        Resources<BidDto> bidDtoCollectionModel = new Resources<>(bidDtoList);

        return ResponseEntity.ok(bidDtoCollectionModel);
    }

    @Override
    public Resource<BidDto> saveBid(Long itemId, int price, Long memberId) {

        Bid bid = new Bid();
        Item item = itemRepository.findById(itemId).orElseThrow(CItemNotFoundException::new);

        bid.setItem(item);
        bid.setMembers(memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new));

        if (item.getCurrentPrice() < price) {
            item.setCurrentPrice(price);
            bid.setPrice(price);
        }

        bidRepository.save(bid);

        BidDto bidDto = modelMapper.map(bid, BidDto.class);
        Resource<BidDto> bidDtoResource = new Resource(bidDto);

        return bidDtoResource;
    }
}
