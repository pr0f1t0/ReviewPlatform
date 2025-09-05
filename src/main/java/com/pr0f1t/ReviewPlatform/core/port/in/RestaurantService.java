package com.pr0f1t.ReviewPlatform.core.port.in;

import com.pr0f1t.ReviewPlatform.core.domain.dto.command.RestaurantCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateCommand command);
}
