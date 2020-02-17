package com.neoga.boltauction.bid.service;

import com.neoga.boltauction.bid.domain.Bid;
import com.neoga.boltauction.bid.dto.BidDto;
import com.neoga.boltauction.bid.repository.BidRepository;
import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Resource;
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
    public List<BidDto> getBidList(Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElseThrow(CItemNotFoundException::new);

        List<Bid> findBidList = bidRepository.findAllByItemOrderByPriceAsc(findItem);
        List<BidDto> bidDtoList = findBidList.stream().map(bid -> modelMapper.map(bid, BidDto.class)).collect(Collectors.toList());

        return bidDtoList;
    }

    @Override
    public Resource<BidDto> saveBid(Long itemId, int price, Long memberId) {

        Bid bid = new Bid();
        Item item = itemRepository.findById(itemId).orElseThrow(CItemNotFoundException::new);

        bid.setItem(item);
        Members findMember = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        bid.setMembers(findMember);

        bidRepository.save(bid);

        BidDto bidDto = modelMapper.map(bid, BidDto.class);
        Resource<BidDto> bidDtoResource = new Resource(bidDto);

        return bidDtoResource;
    }
}
