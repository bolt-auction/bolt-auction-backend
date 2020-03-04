package com.neoga.boltauction.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.util.ItemSerializer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @JsonSerialize(using = ItemSerializer.class)
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatRoomJoin> chatRoomJoins;
}
