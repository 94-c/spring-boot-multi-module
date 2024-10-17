package com.backend.api.config.client;

import com.backend.core.domain.user.data.SocialUserInfoData;
import com.backend.core.domain.user.type.UserSocialType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class NaverClient implements SocialClient {

    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;
    private final RestTemplate restTemplate;

    public NaverClient(
            @Value("${spring.naver.client_id}") String clientId,
            @Value("${spring.naver.redirect_uri}") String redirectUri,
            @Value("${spring.naver.client_secret}") String clientSecret,
            RestTemplate restTemplate) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplate;
    }

    /**
     * Naver 인증 URL을 반환
     * @return Naver 소셜 로그인 URL
     */
    public String getAuthUrl() {
        return String.format(
                "https://nid.naver.com/oauth2.0/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                clientId, redirectUri);
    }

    /**
     * 인가 코드를 받아서 access_token을 반환
     * @param code 인가 코드
     * @return access_token
     */
    @Override
    public String getAccessToken(String code) {
        String reqUrl = "https://nid.naver.com/oauth2.0/token";
        HttpHeaders httpHeaders = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.POST, tokenRequest, String.class);

        JsonObject asJsonObject = JsonParser
                .parseString(Objects.requireNonNull(response.getBody()))
                .getAsJsonObject();

        return asJsonObject.get("access_token").getAsString();
    }

    /**
     * accessToken을 사용하여 Naver 사용자 정보를 조회하여 반환
     * @param accessToken 액세스 토큰
     * @return SocialUserInfoData
     */
    @Override
    public SocialUserInfoData getUserInfo(String accessToken) {
        String reqUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> memberInfoRequest = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.GET, memberInfoRequest, String.class);

        JsonObject jsonObject = JsonParser
                .parseString(Objects.requireNonNull(response.getBody()))
                .getAsJsonObject();
        JsonObject responseObject = jsonObject.getAsJsonObject("response");

        String email = responseObject.get("email").getAsString();
        String providerId = responseObject.get("id").getAsString();

        return SocialUserInfoData.create(email, providerId, UserSocialType.NAVER);
    }
}
