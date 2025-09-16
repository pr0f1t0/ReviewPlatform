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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

        List<Photo> photos = command.getPhotoIds().stream().map(url -> Photo.builder()
                .url(url)
                .uploadTime(now)
                .build()).toList();

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

    @Override
    public Page<Review> listReviews(String restaurantId, Pageable pageable) {

        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        List<Review> reviews = restaurant.getReviews();

        Sort sort = pageable.getSort();

        if(sort.isSorted()){
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            Comparator<Review> comparator = switch (property) {
                case "datePosted" -> Comparator.comparing(Review::getDatePosted);
                case "rating" -> Comparator.comparing(Review::getRating);
                default -> Comparator.comparing(Review::getDatePosted);
            };

            reviews.sort(isAscending ? comparator : comparator.reversed());
        } else{
            reviews.sort(Comparator.comparing(Review::getDatePosted).reversed());
        }

        int start = (int) pageable.getOffset();

        if(start >= reviews.size()){
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        int end = Math.min(start + pageable.getPageSize(), reviews.size());

        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());
    }

    @Override
    public Optional<Review> getReview(String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        return getReviewFromRestaurant(reviewId, restaurant);
    }

    @Override
    public Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateCommand command) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        String authorId = author.getId();
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if(!authorId.equals(existingReview.getWrittenBy().getId())) {
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        if(LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer be edited");
        }

        existingReview.setContent(command.getContent());
        existingReview.setRating(command.getRating());
        existingReview.setLastModified(LocalDateTime.now());
        existingReview.setPhotos(command.getPhotoIds().stream()
                .map(photoId ->
                    Photo.builder()
                            .url(photoId)
                            .uploadTime(LocalDateTime.now())
                            .build()).toList());
        updateRestaurantAverageRating(restaurant);

        List<Review> updatedReviews = restaurant.getReviews().stream()
                .filter(r -> !reviewId.equals(r.getId()))
                .collect(Collectors.toList());
        updatedReviews.add(existingReview);

        restaurant.setReviews(updatedReviews);
        restaurantRepository.save(restaurant);

        return existingReview;
    }

    @Override
    public void deleteReview(User author, String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        String authorId = author.getId();
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if(!authorId.equals(existingReview.getWrittenBy().getId())) {
            throw new ReviewNotAllowedException("Cannot delete another user's review");
        }

        restaurant.getReviews().remove(existingReview);
        updateRestaurantAverageRating(restaurant);
        restaurantRepository.save(restaurant);
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

    private static Optional<Review> getReviewFromRestaurant(String reviewId, Restaurant restaurant) {
        return restaurant.getReviews()
                .stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst();
    }

}
