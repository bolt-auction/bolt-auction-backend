package com.neoga.boltauction.memberstore.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Members {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role;
    @Column(nullable = false, unique = true)
    private String uid;
    @JsonIgnore
    private String passwd;
    private String name;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
    @UpdateTimestamp
    private LocalDateTime changeDt;
    private String provider;
    private String description;
    private String imagePath;
}
