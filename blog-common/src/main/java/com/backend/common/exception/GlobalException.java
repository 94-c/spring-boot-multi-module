package com.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    private final HttpStatus code;

    public GlobalException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }
}
