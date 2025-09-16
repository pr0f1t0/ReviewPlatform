package com.pr0f1t.ReviewPlatform.api.mapper;

import com.pr0f1t.ReviewPlatform.api.dto.request.ReviewCreateUpdateRequest;
import com.pr0f1t.ReviewPlatform.core.domain.dto.command.ReviewCreateUpdateCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewRequestMapper {
    ReviewCreateUpdateCommand toReviewCreateUpdateCommand(ReviewCreateUpdateRequest request);
}
