package com.backend.core.domain.user.exception;

import com.backend.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}
