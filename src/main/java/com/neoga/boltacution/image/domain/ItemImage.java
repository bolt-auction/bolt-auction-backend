package com.neoga.boltacution.image.domain;

import com.neoga.boltacution.item.domain.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ItemImage {
    @Column(name="item_image_id")
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @OneToOne()
    @JoinColumn(name = "image_id")
    private Image image;
    @OneToOne()
    @JoinColumn(name = "item_id")
    private Item item;
}
