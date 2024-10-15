package com.backend.common.response.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(400, "잘못된 요청입니다"),
    UNAUTHORIZED(401, "인증이 필요합니다"),
    FORBIDDEN(403, "권한이 없습니다"),
    NOT_FOUND(404, "요청하신 리소스를 찾을 수 없습니다"),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 메서드입니다"),
    CONFLICT(409, "충돌이 발생했습니다"),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다");

    private final int code;
    private final String message;
}

