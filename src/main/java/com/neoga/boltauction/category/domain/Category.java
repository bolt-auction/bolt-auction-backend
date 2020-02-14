package com.neoga.boltauction.category.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Category {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "sub_category")
    private Category supCategory;
}
