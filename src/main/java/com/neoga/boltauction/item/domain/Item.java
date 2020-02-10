package com.neoga.boltauction.item.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.memberstore.store.domain.Store;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime createDt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime endDt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @Column(columnDefinition = "json")
    private String imagePath;
}
