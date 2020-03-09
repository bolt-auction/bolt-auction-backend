package com.neoga.boltauction.category.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Category {
    @Id
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "sub_category")
    @JsonIgnore
    private Category supCategory;
}
