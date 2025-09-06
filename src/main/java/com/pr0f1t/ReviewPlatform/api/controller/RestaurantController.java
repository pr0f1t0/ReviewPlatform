package com.pr0f1t.ReviewPlatform.api.controller;

import com.pr0f1t.ReviewPlatform.api.dto.request.RestaurantCreateUpdateRequest;
import com.pr0f1t.ReviewPlatform.api.mapper.RestaurantRequestMapper;
import com.pr0f1t.ReviewPlatform.core.domain.dto.RestaurantDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.command.RestaurantCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import com.pr0f1t.ReviewPlatform.core.domain.mapper.RestaurantMapper;
import com.pr0f1t.ReviewPlatform.core.port.in.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantRequestMapper restaurantRequestMapper;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody RestaurantCreateUpdateRequest request) {
        RestaurantCreateUpdateCommand restaurantCreateUpdateRequest = restaurantRequestMapper
                .toCommand(request);

        Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);
        RestaurantDto savedRestaurantDto = restaurantMapper.toRestaurantDto(restaurant);

        return ResponseEntity.ok(savedRestaurantDto);
    }

}
