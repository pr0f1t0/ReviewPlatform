package com.pr0f1t.ReviewPlatform.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateUpdateRequest {

    private String title;

    private Integer rating;

    private List<String> photoIds;
}
