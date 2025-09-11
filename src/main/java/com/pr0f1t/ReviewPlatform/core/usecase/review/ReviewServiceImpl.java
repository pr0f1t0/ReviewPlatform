package com.pr0f1t.ReviewPlatform.core.usecase.review;

import com.pr0f1t.ReviewPlatform.core.domain.dto.command.ReviewCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Photo;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Review;
import com.pr0f1t.ReviewPlatform.core.domain.entity.User;
import com.pr0f1t.ReviewPlatform.core.exception.RestaurantNotFoundException;
import com.pr0f1t.ReviewPlatform.core.exception.ReviewNotAllowedException;
import com.pr0f1t.ReviewPlatform.core.port.in.ReviewService;
import com.pr0f1t.ReviewPlatform.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;
    @Override
    public Review createReview(User author, String restaurant_id, ReviewCreateUpdateCommand command) {
        Restaurant restaurant = getRestaurantOrThrow(restaurant_id);

        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if (hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");
        }

        LocalDateTime now = LocalDateTime.now();

        List<Photo> photos = command.getPhotoIds().stream().map(url -> {
            return Photo.builder()
                    .url(url)
                    .uploadTime(now)
                    .build();
                }).toList();

        String reviewId = UUID.randomUUID().toString();

        Review reviewToCreate = Review.builder()
                .id(reviewId)
                .content(command.getContent())
                .rating(command.getRating())
                .photos(photos)
                .datePosted(now)
                .lastModified(now)
                .writtenBy(author)
                .build();

        restaurant.getReviews().add(reviewToCreate);

        updateRestaurantAverageRating(restaurant);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return savedRestaurant.getReviews().stream().filter(review -> review.getId()
                .equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error retrieving created review"));
    }

    private Restaurant getRestaurantOrThrow(String restaurant_id) {
        return restaurantRepository.findById(restaurant_id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id not found: " + restaurant_id));
    }

    private void updateRestaurantAverageRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();

        if(reviews.isEmpty()) {
            restaurant.setAverageRating(0.0f);
        }else{
            double averageRating = reviews.stream().mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0f);
            restaurant.setAverageRating((float) averageRating);
        }

    }

}
