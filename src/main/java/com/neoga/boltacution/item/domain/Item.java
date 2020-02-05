package com.neoga.boltacution.item.domain;

import com.neoga.boltacution.category.domain.Category;
import com.neoga.boltacution.memberstore.store.domain.Store;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Item {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int quick_price;
    private int start_price;
    private int min_bid_price;
    private boolean is_sell;
    private LocalDateTime create_date;
    private LocalDateTime end_date;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
