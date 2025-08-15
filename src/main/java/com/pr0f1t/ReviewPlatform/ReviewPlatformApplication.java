package com.pr0f1t.ReviewPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReviewPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewPlatformApplication.class, args);
	}

}
