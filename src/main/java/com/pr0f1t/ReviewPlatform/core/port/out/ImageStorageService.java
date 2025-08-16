package com.pr0f1t.ReviewPlatform.core.port.out;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ImageStorageService {
    CompletableFuture<String> store(MultipartFile file, String fileName);
    CompletableFuture<Optional<Resource>> load(String id);
}
