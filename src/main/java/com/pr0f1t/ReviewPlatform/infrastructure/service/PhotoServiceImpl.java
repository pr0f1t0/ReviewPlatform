package com.pr0f1t.ReviewPlatform.infrastructure.service;

import com.pr0f1t.ReviewPlatform.core.domain.entity.Photo;
import com.pr0f1t.ReviewPlatform.core.port.out.PhotoService;
import com.pr0f1t.ReviewPlatform.core.port.out.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;


    @Override
    public CompletableFuture<Photo> uploadPhotoAsync(MultipartFile file) {

        String photoId =  UUID.randomUUID().toString();

        return storageService.storeAsync(file, photoId)
                .thenApply(url -> Photo.builder()
                        .url(url)
                        .uploadTime(LocalDateTime.now())
                        .build()
                );

    }


    @Override
    public CompletableFuture<Optional<Resource>> getPhotoAsResourceAsync(String id) {
        return storageService.loadAsResourceAsync(id);
    }
}
