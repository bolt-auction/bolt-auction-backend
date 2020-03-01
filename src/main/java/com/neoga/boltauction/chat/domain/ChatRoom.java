package com.neoga.boltauction.chat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class ChatRoom {
    @JsonProperty("chatRoomId")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NonNull
    @JsonProperty("chatRoomName")
    private String name;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createDt;
}
