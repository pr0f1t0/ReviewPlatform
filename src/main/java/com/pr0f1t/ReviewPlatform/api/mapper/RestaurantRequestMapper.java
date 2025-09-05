package com.pr0f1t.ReviewPlatform.api.mapper;

import com.pr0f1t.ReviewPlatform.api.dto.request.RestaurantCreateUpdateRequest;
import com.pr0f1t.ReviewPlatform.core.domain.dto.command.RestaurantCreateUpdateCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantRequestMapper {
    public RestaurantCreateUpdateCommand toCommand(RestaurantCreateUpdateRequest request);
}
