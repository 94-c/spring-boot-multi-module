package com.backend.api.domain.auth.controller;

import com.backend.api.domain.auth.dto.request.CreateUserRequest;
import com.backend.api.domain.auth.service.AuthService;
import com.backend.common.response.SuccessResponse;
import com.backend.common.response.code.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Long>> register(@Valid @RequestBody CreateUserRequest request) {
        Long userId = authService.create(request);
        SuccessResponse<Long> response = SuccessResponse.of(SuccessCode.OK, userId);
        return ResponseEntity.ok(response);
    }


}
