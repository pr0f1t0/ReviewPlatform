package com.pr0f1t.ReviewPlatform.core.port.in;

import com.pr0f1t.ReviewPlatform.core.domain.dto.command.RestaurantCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateCommand command);

    Page<Restaurant> searchRestaurants(String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable);
}
