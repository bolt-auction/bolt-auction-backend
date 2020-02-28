package com.neoga.boltauction.bid.service;

import com.neoga.boltauction.bid.domain.Bid;
import com.neoga.boltauction.bid.dto.BidDto;
import com.neoga.boltauction.bid.repository.BidRepository;
import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    @Override
    public List<BidDto> getBidList(Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElseThrow(CItemNotFoundException::new);

        List<Bid> findBidList = bidRepository.findAllByItemOrderByPriceAsc(findItem);
        List<BidDto> bidDtoList = findBidList.stream().map(bid -> mapBidBidDto(bid)).collect(Collectors.toList());

        return bidDtoList;
    }

    @Override
    public BidDto saveBid(Long itemId, int price, Long memberId) {

        Bid bid = new Bid();
        Item item = itemRepository.getOne(itemId);

        bid.setItem(item);
        Members findMember = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        bid.setMembers(findMember);

        bid.setPrice(price);

        bidRepository.save(bid);

        return mapBidBidDto(bid);
    }

    private BidDto mapBidBidDto(Bid bid) {
        BidDto bidDto = modelMapper.map(bid, BidDto.class);
        bidDto.setBidId(bid.getId());
        bidDto.setMemberId(bid.getMembers().getId());
        bidDto.setStoreId(bid.getMembers().getStore().getId());

        return bidDto;
    }
}
