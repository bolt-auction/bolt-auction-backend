package com.neoga.platform.chat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoga.platform.memberstore.member.domain.Members;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoomJoin {
    @JsonProperty("chatRoomJoinId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members participant;
}

