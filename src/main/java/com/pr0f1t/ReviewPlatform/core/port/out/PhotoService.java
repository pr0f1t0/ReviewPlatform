package com.pr0f1t.ReviewPlatform.core.port.out;

import com.pr0f1t.ReviewPlatform.core.domain.entity.Photo;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PhotoService {
    CompletableFuture<Photo> uploadPhotoAsync(MultipartFile file);
    CompletableFuture<Optional<Resource>> getPhotoAsResourceAsync(String id);
}
