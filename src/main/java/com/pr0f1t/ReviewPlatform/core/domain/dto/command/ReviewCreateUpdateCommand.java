package com.pr0f1t.ReviewPlatform.core.domain.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateUpdateCommand {
    private String content;

    private Integer rating;

    private List<String> photoIds;
}
