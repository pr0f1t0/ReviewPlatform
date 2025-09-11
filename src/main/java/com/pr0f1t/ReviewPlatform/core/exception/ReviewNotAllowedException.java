package com.pr0f1t.ReviewPlatform.core.exception;

public class ReviewNotAllowedException extends BaseException {
    public ReviewNotAllowedException() {
    }

    public ReviewNotAllowedException(String message) {
        super(message);
    }

    public ReviewNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewNotAllowedException(Throwable cause) {
        super(cause);
    }
}
