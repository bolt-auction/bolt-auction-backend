package com.neoga.boltauction.memberstore.review.domain;

import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.store.domain.Store;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private String content;
    private Members register;
    private Store store;
    private LocalDateTime createDt;
}
