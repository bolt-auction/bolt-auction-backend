package com.neoga.platform.bid.service;

import com.neoga.platform.bid.domain.Bid;
import com.neoga.platform.bid.dto.BidDto;
import com.neoga.platform.bid.dto.Register;
import com.neoga.platform.bid.repository.BidRepository;
import com.neoga.platform.exception.custom.CItemNotFoundException;
import com.neoga.platform.exception.custom.CMemberNotFoundException;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.repository.ItemRepository;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new CItemNotFoundException("상품이 존재하지 않습니다."));

        List<Bid> findBidList = bidRepository.findAllByItemOrderByPriceAsc(findItem);

        return findBidList.stream().map(this::mapBidBidDto).collect(Collectors.toList());
    }

    @Override
    public BidDto saveBid(Long itemId, int price, Long memberId) {

        Bid bid = new Bid();
        Item item = itemRepository.getOne(itemId);
        item.setCurrentPrice(price);
        item.setBidCount(item.getBidCount() + 1);

        bid.setItem(item);
        Members findMember = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        bid.setMembers(findMember);

        bid.setPrice(price);

        bidRepository.save(bid);
        itemRepository.save(item);

        return mapBidBidDto(bid);
    }

    @Override
    public Long getMemberByBidId(Long bidId) {
        return bidRepository.getOne(bidId).getMembers().getId();
    }

    @Override
    public void deleteBid(Long bidId) {
        bidRepository.deleteById(bidId);
    }

    private BidDto mapBidBidDto(Bid bid) {
        BidDto bidDto = modelMapper.map(bid, BidDto.class);
        bidDto.setBidId(bid.getId());

        Members members = bid.getMembers();
        Register register = new Register();
        register.setMemberId(members.getId());
        register.setMemberName(members.getName());
        register.setMemberImagePath(members.getImagePath());

        bidDto.setMember(register);

        bidDto.setItemId(bid.getItem().getId());

        return bidDto;
    }

    @Override
    public Optional<Bid> getMaxBidByItemId(Long itemId){
        Item item = itemRepository.getOne(itemId);
        List<Bid> bid = bidRepository.findAllByItemOrderByPriceDesc(item);

        if(bid.isEmpty())
            return Optional.empty();

        return Optional.of(bid.get(0));
    }
}
