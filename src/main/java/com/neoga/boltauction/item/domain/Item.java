package com.neoga.boltauction.item.domain;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.memberstore.store.domain.Store;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Item {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int quickPrice;
    private int startPrice;
    private int minBidPrice;
    private boolean isSell = false;
    private LocalDateTime createDt;
    private LocalDateTime endDt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
