package com.neoga.autionnara.memberstore.review.domain;

import com.neoga.autionnara.memberstore.member.domain.Members;
import com.neoga.autionnara.memberstore.store.domain.Store;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Review {
    @Column(name="review_id")
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private Members register;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
}
