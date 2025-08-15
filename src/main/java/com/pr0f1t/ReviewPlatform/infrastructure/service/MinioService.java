package com.pr0f1t.ReviewPlatform.infrastructure.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioService(MinioClient minioClient,
                        @Value("${minio.bucket}") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    @Async
    public CompletableFuture<Void> uploadAsync(String fileName, InputStream data, String contentType) {
        try {
            boolean found = minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder()
                            .bucket(bucket)
                            .build()
            );
            if (!found) {
                minioClient.makeBucket(
                        io.minio.MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build()
                );
            }

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(data, -1, 10 * 1024 * 1024)
                    .contentType(contentType)
                    .build());

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}

