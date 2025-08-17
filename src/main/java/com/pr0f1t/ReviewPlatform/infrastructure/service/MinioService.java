package com.pr0f1t.ReviewPlatform.infrastructure.service;

import com.pr0f1t.ReviewPlatform.core.exception.StorageException;
import com.pr0f1t.ReviewPlatform.core.port.out.StorageService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MinioService implements StorageService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioService(MinioClient minioClient,
                        @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    @Override
    @Async
    public CompletableFuture<String> storeAsync(MultipartFile file, String filename) {
        if (file.isEmpty()) {
            return CompletableFuture.failedFuture(new StorageException("Cannot save empty file"));
        }
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }


            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(filename)
                                .stream(is, -1, 10 * 1024 * 1024)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            return CompletableFuture.completedFuture(filename);

        } catch (Exception e) {
            return CompletableFuture.failedFuture(new StorageException("Could not upload a file", e));
        }
    }

    @Override
    @Async
    public CompletableFuture<Optional<Resource>> loadAsResourceAsync(String fileName) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build()
            );
            return CompletableFuture.completedFuture(Optional.of(new InputStreamResource(stream)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }
}

