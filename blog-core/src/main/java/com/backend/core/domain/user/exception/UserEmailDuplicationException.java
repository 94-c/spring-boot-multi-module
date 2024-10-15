package com.backend.core.domain.user.exception;

import com.backend.common.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class UserEmailDuplicationException extends GlobalException {
    public UserEmailDuplicationException(HttpStatus statusCode, String message) {
        super(statusCode, message);
    }
}
