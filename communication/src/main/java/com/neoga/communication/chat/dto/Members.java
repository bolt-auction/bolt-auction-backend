package com.neoga.communication.chat.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Members {
    private Long id;
    private List<String> role;
    private String uid;
    private String passwd;
    private String name;
    private LocalDateTime createDt;
    private LocalDateTime changeDt;
    private String provider;
    private String description;
    private String imagePath;
}
