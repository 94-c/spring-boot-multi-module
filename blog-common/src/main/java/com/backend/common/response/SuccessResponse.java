package com.backend.common.response;

import com.backend.common.response.code.SuccessCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;



@Getter
public class SuccessResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public SuccessResponse(SuccessCode successCode, T data) {
        this.code = successCode.getCode();
        this.message = successCode.getMessage();
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(SuccessCode successCode, T data) {
        return new SuccessResponse<>(successCode, data);
    }
}
