package com.pr0f1t.ReviewPlatform.api.controller;


import com.pr0f1t.ReviewPlatform.api.dto.request.ReviewCreateUpdateRequest;
import com.pr0f1t.ReviewPlatform.api.mapper.ReviewRequestMapper;
import com.pr0f1t.ReviewPlatform.core.domain.dto.ReviewDto;
import com.pr0f1t.ReviewPlatform.core.domain.dto.command.ReviewCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Review;
import com.pr0f1t.ReviewPlatform.core.domain.entity.User;
import com.pr0f1t.ReviewPlatform.core.domain.mapper.ReviewMapper;
import com.pr0f1t.ReviewPlatform.core.port.in.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRequestMapper reviewRequestMapper;
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequest review,
            @AuthenticationPrincipal Jwt jwt
            ){

        ReviewCreateUpdateCommand command = reviewRequestMapper.toReviewCreateUpdateCommand(review);

        User user = jwtToUser(jwt);

        Review createdReview = reviewService.createReview(user, restaurantId, command);

        return ResponseEntity.ok(reviewMapper.toReviewDto(createdReview));
    }

    @GetMapping
    public Page<ReviewDto> getReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC) Pageable pageable
    ){
        return reviewService.listReviews(restaurantId, pageable)
                .map(reviewMapper::toReviewDto);
    }

    @GetMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ){
        return reviewService.getReview(restaurantId, reviewId)
                .map(reviewMapper::toReviewDto)
                .map(ResponseEntity::ok)
                .orElseGet(()  -> ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ){
        ReviewCreateUpdateCommand command = reviewRequestMapper.toReviewCreateUpdateCommand(request);
        User user = jwtToUser(jwt);

        Review updatedReview = reviewService.updateReview(user, restaurantId, reviewId, command);

        return ResponseEntity.ok(reviewMapper.toReviewDto(updatedReview));
    }

    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @AuthenticationPrincipal Jwt jwt
    ){
        User user =  jwtToUser(jwt);
        reviewService.deleteReview(user, restaurantId, reviewId);

        return ResponseEntity.noContent().build();
    }

    private User jwtToUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}
