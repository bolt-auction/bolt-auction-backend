package com.neoga.platform.order.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.util.ItemIdSerializer;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.util.MemberIdSerializer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonSerialize(using = MemberIdSerializer.class)
    @JsonUnwrapped
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members members;
    @JsonSerialize(using = ItemIdSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    private int price;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
}