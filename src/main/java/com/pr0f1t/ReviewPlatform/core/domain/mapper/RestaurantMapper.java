package com.pr0f1t.ReviewPlatform.core.domain.mapper;

import com.pr0f1t.ReviewPlatform.core.domain.dto.AddressDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.GeoPointDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.RestaurantDto;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Address;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);

    AddressDto  toAddressDto(Address address);
}
