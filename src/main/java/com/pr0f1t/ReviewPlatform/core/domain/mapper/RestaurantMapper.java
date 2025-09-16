package com.pr0f1t.ReviewPlatform.core.domain.mapper;

import com.pr0f1t.ReviewPlatform.core.domain.dto.AddressDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.GeoPointDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.RestaurantDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.RestaurantSummaryDto;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Address;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Review;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantSummaryDto toSummaryDto(Restaurant restaurant);

    @Named("populateTotalReviews")
    default Integer populateTotalReviews(@NotNull List<Review> reviews) {
        return reviews.size();
    }

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);

    AddressDto  toAddressDto(Address address);
}
