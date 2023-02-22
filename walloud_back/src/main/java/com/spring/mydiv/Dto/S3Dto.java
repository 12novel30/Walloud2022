package com.spring.mydiv.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class S3Dto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageUrls {
        private String deleteImage;
        private String newImage;
    }
}
