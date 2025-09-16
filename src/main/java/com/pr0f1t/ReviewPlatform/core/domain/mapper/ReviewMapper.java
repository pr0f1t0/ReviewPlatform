package com.pr0f1t.ReviewPlatform.core.domain.mapper;

import com.pr0f1t.ReviewPlatform.core.domain.dto.ReviewDto;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewDto toReviewDto(Review review);
}
