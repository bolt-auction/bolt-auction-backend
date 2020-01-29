package com.neoga.autionnara.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Image {
    @Column(name="image_id")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String image_path;
    private LocalDateTime create_date;
}
