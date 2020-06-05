package com.neoga.communication.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Notification {
    @JsonProperty("notificationId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private NotifyType type;
    private String content;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime create_dt;
    private boolean is_read=false;
    @JsonIgnore
    private Long receiverId;
}
