package com.neoga.platform.communication.chat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoga.platform.communication.chat.util.ChatRoomSerializer;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.util.MemberSerializer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    @JsonProperty("chatMessageId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("chatMessageContent")
    private String content;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createDt;
    private boolean isRead = false;
    @JsonSerialize(using = ChatRoomSerializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;
    @JsonSerialize(using = MemberSerializer.class)
    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Members sender;
}
