package com.pr0f1t.ReviewPlatform.infrastructure.service;

import com.pr0f1t.ReviewPlatform.core.port.out.ImageStorageService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MinioService implements ImageStorageService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioService(MinioClient minioClient,
                        @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    @Override
    @Async
    public CompletableFuture<String> store(MultipartFile file, String filename) {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            // Use given filename or generate one
            String objectName = (filename != null && !filename.isBlank())
                    ? filename
                    : UUID.randomUUID() + "-" + file.getOriginalFilename();

            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .stream(is, -1, 10 * 1024 * 1024)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            return CompletableFuture.completedFuture(objectName);

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @Async
    public CompletableFuture<Optional<Resource>> load(String id) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(id)
                            .build()
            );
            return CompletableFuture.completedFuture(Optional.of(new InputStreamResource(stream)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }
}

