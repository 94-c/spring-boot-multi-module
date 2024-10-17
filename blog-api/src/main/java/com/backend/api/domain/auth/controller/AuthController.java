package com.backend.api.domain.auth.controller;

import com.backend.api.domain.auth.dto.request.CreateUserRequest;
import com.backend.api.domain.auth.dto.request.SocialLoginRequest;
import com.backend.api.domain.auth.dto.response.LoginResponse;
import com.backend.api.domain.auth.service.AuthService;
import com.backend.common.response.SuccessResponse;
import com.backend.common.response.code.SuccessCode;
import com.backend.core.domain.user.User;
import com.backend.core.domain.user.data.SocialUserInfoData;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/{provider}")
    public ResponseEntity<Void> login(@PathVariable("provider") String provider, HttpServletResponse response) throws IOException {
        String authUrl;
        try {
            authUrl = authService.getAuthUrl(provider);  // AuthService에서 provider에 따라 인증 URL 생성
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();  // 지원하지 않는 provider일 경우 400 Bad Request 반환
        }

        response.sendRedirect(authUrl);  // 인증 URL로 리다이렉트
        return ResponseEntity.status(HttpServletResponse.SC_FOUND).build();  // 302 리다이렉트 응답 반환
    }

    @PostMapping("/social-login")
    public ResponseEntity<SuccessResponse<LoginResponse>> socialLogin(@RequestBody SocialLoginRequest request) {
        SocialUserInfoData userInfo = request.getUserInfo();
        if (!authService.isUserExists(userInfo)) {
            Long userId = authService.registerUserWithAdditionalInfo(userInfo, request.getCreateUserRequest());
            User newUser = authService.findUserById(userId);
            String jwtToken = authService.generateJwtToken(newUser);

            LoginResponse loginResponse = new LoginResponse(jwtToken, newUser.getEmail(), newUser.getNickname(), newUser.getSocial());
            return ResponseEntity.ok(SuccessResponse.of(SuccessCode.CREATED, loginResponse));
        }

        User existingUser = authService.findUserByEmail(userInfo.getEmail());
        String jwtToken = authService.generateJwtToken(existingUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, existingUser.getEmail(), existingUser.getNickname(), existingUser.getSocial());
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.OK, loginResponse));
    }

}
