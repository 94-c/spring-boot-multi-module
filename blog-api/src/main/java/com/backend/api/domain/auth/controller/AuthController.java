package com.backend.api.domain.auth.controller;

import com.backend.api.domain.auth.dto.request.CreateUserRequest;
import com.backend.api.domain.auth.dto.request.SocialLoginRequest;
import com.backend.api.domain.auth.dto.response.LoginResponse;
import com.backend.api.domain.auth.service.AuthService;
import com.backend.common.response.SuccessResponse;
import com.backend.common.response.code.SuccessCode;
import com.backend.core.domain.user.User;
import com.backend.core.domain.user.data.SocialUserInfoData;
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
