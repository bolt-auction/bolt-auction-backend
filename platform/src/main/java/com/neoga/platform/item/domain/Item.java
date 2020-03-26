package com.neoga.platform.item.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoga.platform.category.domain.Category;
import com.neoga.platform.category.util.CategorySerializer;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.util.MemberSerializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("itemId")
    private Long id;
    @JsonProperty("itemName")
    private String name;
    private String description;
    private int quickPrice;
    private int startPrice;
    private int minBidPrice;
    private int currentPrice;
    private boolean isEnd = false;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonSerialize(using = CategorySerializer.class)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonProperty("seller")
    @JsonSerialize(using = MemberSerializer.class)
    private Members members;
    private String imagePath;
    private int bidCount;
}
