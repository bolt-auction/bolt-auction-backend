package com.neoga.boltauction.chat.domain;


import com.neoga.boltauction.memberstore.member.domain.Members;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoomJoin {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members participant;
}