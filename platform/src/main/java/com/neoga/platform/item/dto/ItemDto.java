package com.neoga.platform.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoga.platform.category.domain.Category;
import com.neoga.platform.category.util.CategorySerializer;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.util.MemberSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.Future;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Relation(collectionRelation = "itemList")
public class ItemDto {

    private Long itemId;
    private String itemName;
    private String description;
    @Positive
    private int quickPrice;
    @Positive
    private int startPrice;
    @Positive
    private int minBidPrice;
    private int currentPrice;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Future
    private LocalDateTime endDt;
    @JsonSerialize(using = CategorySerializer.class)
    private Category category;
    private String[] imagePath;
    @JsonSerialize(using = MemberSerializer.class)
    private Members seller;
    private Long bidCount;
    private boolean isEnd;
}
