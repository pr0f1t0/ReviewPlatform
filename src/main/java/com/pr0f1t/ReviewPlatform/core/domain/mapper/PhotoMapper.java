package com.pr0f1t.ReviewPlatform.core.domain.mapper;

import com.pr0f1t.ReviewPlatform.core.domain.dto.PhotoDto;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {
    PhotoDto toDto(Photo photo);
}
