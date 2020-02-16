package com.neoga.boltauction.memberstore.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neoga.boltauction.memberstore.member.domain.Members;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String description;
    @OneToOne(mappedBy="store", fetch = FetchType.LAZY)
    @JsonIgnore
    private Members members;
    @Column(columnDefinition = "json")
    private String imagePath;

    public void changeMembers(Members members) {
        this.members = members;
        members.setStore(this);
    }
}
