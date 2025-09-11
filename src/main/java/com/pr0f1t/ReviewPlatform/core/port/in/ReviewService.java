package com.pr0f1t.ReviewPlatform.core.port.in;


import com.pr0f1t.ReviewPlatform.core.domain.dto.command.ReviewCreateUpdateCommand;
import com.pr0f1t.ReviewPlatform.core.domain.entity.Review;
import com.pr0f1t.ReviewPlatform.core.domain.entity.User;

public interface ReviewService {
    Review createReview(User author, String restaurant_id, ReviewCreateUpdateCommand command);
}
