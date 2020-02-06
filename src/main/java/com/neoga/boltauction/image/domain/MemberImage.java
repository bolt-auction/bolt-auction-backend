package com.neoga.boltauction.image.domain;

import com.neoga.boltauction.memberstore.member.domain.Members;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class MemberImage {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @OneToOne()
    @JoinColumn(name = "image_id")
    private Image image;
    @OneToOne()
    @JoinColumn(name = "member_id")
    private Members members;
}
