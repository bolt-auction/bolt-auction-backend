package com.neoga.boltauction.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoProfile {
    private Long id;
    private Properties properties;

    @Getter
    @Setter
    private static class Properties {
        private String nickname;
        private String thumbnail_image;
        private String profile_image;
    }
}
