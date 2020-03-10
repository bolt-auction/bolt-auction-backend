package com.neoga.boltauction.bid.domain;

import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.memberstore.member.domain.Members;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members members;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    private int price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
}
