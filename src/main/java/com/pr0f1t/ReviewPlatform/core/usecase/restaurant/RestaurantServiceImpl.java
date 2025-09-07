package com.pr0f1t.ReviewPlatform.core.usecase.restaurant;

import com.pr0f1t.ReviewPlatform.core.domain.dto.command.RestaurantCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Address;
import com.pr0f1t.ReviewPlatform.core.domain.entity.GeoLocation;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Photo;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import com.pr0f1t.ReviewPlatform.core.port.in.RestaurantService;
import com.pr0f1t.ReviewPlatform.core.port.out.GeoLocationService;
import com.pr0f1t.ReviewPlatform.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateCommand command) {
        Address address = command.getAddress();
        GeoLocation geoLocation = geoLocationService.getGeoLocation(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = command.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(photoUrl -> Photo.builder()
                .url(photoUrl)
                .uploadTime(LocalDateTime.now())
                .build()).toList();

        Restaurant restaurant = Restaurant.builder()
                .name(command.getName())
                .cuisineType(command.getCuisineType())
                .contactInformation(command.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(command.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();


        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> searchRestaurants(String query, Float minRating, Float latitude, Float longitude,
                                              Float radius, Pageable pageable) {
        if(minRating != null && (query == null || query.isEmpty())) {
            restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = minRating == null ? 0f : minRating;

        if(query != null && !query.trim().isEmpty()) {
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        if(latitude != null && longitude != null && radius != null) {
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);
    }
}
