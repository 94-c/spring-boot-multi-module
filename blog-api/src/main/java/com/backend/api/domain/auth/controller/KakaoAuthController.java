package com.backend.api.domain.auth.controller;

import com.backend.api.config.client.KakaoClient;
import com.backend.core.domain.user.data.SocialUserInfoData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {
    private final KakaoClient kakaoClient;
    private final String kakaoClientId = "4aba0b53045ca21c1bc18df1001d8f00"; // application.yml의 값
    private final String kakaoRedirectUri = "http://localhost:9081/auth/kakao/callback"; // application.yml의 값

    /**
     * 사용자에게 카카오 로그인 페이지로 리다이렉트
     */
    @GetMapping("/login")
    public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = String.format(
                "https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                kakaoClientId, kakaoRedirectUri);

        response.sendRedirect(kakaoAuthUrl); // 카카오 로그인 페이지로 리다이렉트
    }

    /**
     * 카카오 로그인 후, 리다이렉트된 URI로 인가 코드를 받아 액세스 토큰 요청
     * @param code 인가 코드
     * @return 사용자 정보 및 액세스 토큰
     */
    @GetMapping("/callback")
    public ResponseEntity<?> kakaoLoginCallback(@RequestParam String code) {
        try {
            // 인가 코드를 사용해 액세스 토큰 발급
            String accessToken = kakaoClient.getAccessToken(code);

            // 액세스 토큰을 사용해 사용자 정보 가져오기
            SocialUserInfoData userInfo = kakaoClient.getUserInfo(accessToken);

            // 응답으로 사용자 정보와 액세스 토큰을 반환
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", accessToken);
            response.put("user_info", userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during Kakao login: " + e.getMessage());
        }
    }
}
