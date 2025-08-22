package com.pr0f1t.ReviewPlatform.api.controller;

import com.pr0f1t.ReviewPlatform.core.domain.dto.PhotoDto;
import com.pr0f1t.ReviewPlatform.core.domain.mapper.PhotoMapper;
import com.pr0f1t.ReviewPlatform.core.port.out.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @PostMapping
    public CompletableFuture<ResponseEntity<PhotoDto>> uploadPhoto(@RequestParam("file")MultipartFile file) {
        log.info("Uploading file {}", file.getOriginalFilename());
        return photoService.uploadPhotoAsync(file)
                .thenApply(photoMapper::toDto)
                .thenApply(photoDto -> ResponseEntity.ok(photoDto));

    }

    @GetMapping(path = "/{id:.+}")
    public CompletableFuture<ResponseEntity<Resource>> getPhoto(@PathVariable String id) {

        return photoService.getPhotoAsResourceAsync(id).thenApply(photoResource ->
                photoResource.map(photo ->
                        ResponseEntity.ok()
                                .contentType(MediaTypeFactory.getMediaType(photo)
                                        .orElse(MediaType.APPLICATION_OCTET_STREAM))
                                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                                .body(photo)
                ).orElse(ResponseEntity.notFound().build())
        );

    }
}
