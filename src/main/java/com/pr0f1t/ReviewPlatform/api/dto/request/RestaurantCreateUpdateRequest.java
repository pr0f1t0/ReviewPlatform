package com.pr0f1t.ReviewPlatform.api.dto.request;

import com.pr0f1t.ReviewPlatform.core.domain.entity.Address;
import com.pr0f1t.ReviewPlatform.core.domain.entity.OperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequest {
    private String name;
    private String cuisineType;
    private String contactInformation;
    private Address address;
    private OperatingHours operatingHours;
    private List<String> photoIds;
}
