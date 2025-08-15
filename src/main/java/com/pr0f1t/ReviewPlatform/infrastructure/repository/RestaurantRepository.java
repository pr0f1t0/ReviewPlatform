package com.pr0f1t.ReviewPlatform.infrastructure.repository;

import com.pr0f1t.ReviewPlatform.core.domain.entity.Restaurant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {
}
