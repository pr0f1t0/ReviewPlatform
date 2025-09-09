package com.pr0f1t.ReviewPlatform.api.controller;

import com.pr0f1t.ReviewPlatform.api.dto.request.RestaurantCreateUpdateRequest;
import com.pr0f1t.ReviewPlatform.api.mapper.RestaurantRequestMapper;
import com.pr0f1t.ReviewPlatform.core.domain.dto.RestaurantDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.RestaurantSummaryDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.command.RestaurantCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import com.pr0f1t.ReviewPlatform.core.domain.mapper.RestaurantMapper;
import com.pr0f1t.ReviewPlatform.core.port.in.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Page<RestaurantSummaryDto> searchRestaurants(@RequestParam(required = false) String q,
                                                        @RequestParam(required = false) Float minRating,
                                                        @RequestParam(required = false) Float latitude,
                                                        @RequestParam(required = false) Float longitude,
                                                        @RequestParam(required = false) Float radius,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "20") int size
    ){
        Page<Restaurant> searchResults =  restaurantService.searchRestaurants(
                q, minRating, latitude, longitude, radius, PageRequest.of(page - 1, size)
        );
        return searchResults.map(restaurantMapper::toSummaryDto);
    }

    @GetMapping(path = "/{restaurant_id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable("restaurant_id") String restaurant_id){
        return restaurantService.getRestaurant(restaurant_id)
                .map(restaurant -> ResponseEntity.ok(restaurantMapper.toRestaurantDto(restaurant)))
                .orElse(ResponseEntity.notFound().build());
    }
}
