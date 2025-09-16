package com.pr0f1t.ReviewPlatform.core.domain.dto;

import com.pr0f1t.ReviewPlatform.core.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {

    private String id;

    private String name;

    private String cuisineType;

    private String contactInformation;

    private Float averageRating;

    private GeoPointDto geoLocation;

    private AddressDto address;

    private OperatingHours operatingHours;

    private List<PhotoDto> photos =  new ArrayList<>();

    private List<Review> reviews = new ArrayList<>();

    private UserDto createdBy;

    private Integer totalReviews;
}
