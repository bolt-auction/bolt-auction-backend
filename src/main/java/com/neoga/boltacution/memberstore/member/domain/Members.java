package com.neoga.boltacution.memberstore.member.domain;

import com.neoga.boltacution.memberstore.store.domain.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Members {
    @Column(name="member_id")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String passwd;
    private String name;
    @Column(name="create_date", updatable = false)
    private LocalDateTime createDate = LocalDateTime.now();
    @Column(name="change_date")
    private LocalDateTime changeDate = LocalDateTime.now();
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    private String provider;
}
