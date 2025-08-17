package com.pr0f1t.ReviewPlatform.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDto {

    private String url;

    private LocalDateTime uploadTime;
}
