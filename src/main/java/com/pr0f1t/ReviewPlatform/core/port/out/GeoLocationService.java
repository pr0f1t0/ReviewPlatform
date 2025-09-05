package com.pr0f1t.ReviewPlatform.core.port.out;

import com.pr0f1t.ReviewPlatform.core.domain.entity.Address;
import com.pr0f1t.ReviewPlatform.core.domain.entity.GeoLocation;

public interface GeoLocationService {
    GeoLocation getGeoLocation(Address address);
}
