package com.backend.common.response.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    OK(200, "요청이 성공적으로 처리되었습니다"),
    CREATED(201, "리소스가 성공적으로 생성되었습니다"),
    ACCEPTED(202, "요청이 접수되었으며 처리 중입니다"),
    NO_CONTENT(204, "반환할 내용이 없습니다");

    private final int code;
    private final String message;
}
