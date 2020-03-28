package com.neoga.platform.event;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ReviewAddEvent implements Serializable {
    private Long receiverId;
    private Long registerId;
    private String content;
    private LocalDateTime createDt;
}
