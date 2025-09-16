package com.pr0f1t.ReviewPlatform.core.port.in;


import com.pr0f1t.ReviewPlatform.core.domain.dto.command.ReviewCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Review;
import com.pr0f1t.ReviewPlatform.core.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {
    Review createReview(User author, String restaurant_id, ReviewCreateUpdateCommand command);

    Page<Review> listReviews(String restaurantId, Pageable pageable);

    Optional<Review> getReview(String restaurantId, String reviewId);

    Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateCommand command);

    void deleteReview(User author, String restaurantId, String reviewId);
}

