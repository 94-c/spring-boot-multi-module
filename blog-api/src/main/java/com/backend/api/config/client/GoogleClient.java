package com.backend.api.config.client;

import com.backend.core.domain.user.data.SocialUserInfoData;
import com.backend.core.domain.user.type.UserSocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleClient implements SocialClient {
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public GoogleClient(@Value("${spring.google.client_id}") String clientId,
                        @Value("${spring.google.redirect_uri}") String redirectUri,
                        @Value("${spring.google.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    /**
     * Google 인증 URL을 반환
     * @return Google 소셜 로그인 URL
     */
    public String getAuthUrl() {
        return String.format(
                "https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=email profile",
                clientId, redirectUri);
    }

    @Override
    public String getAccessToken(String code, String state) {
        throw new UnsupportedOperationException("구글은 state를 지원하지 않습니다.");
    }

    /**
     * 인가 코드를 받아서 access_token을 반환
     * @param code
     * @return access_token
     * @throws JsonProcessingException
     */
    @Override
    public String getAccessToken(String code) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://oauth2.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        String responseBody = webClient.post()
                .uri("/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    /**
     * accessToken을 사용하여 Google 사용자 정보를 조회하여 반환
     * @param accessToken
     * @return SocialUserInfoData
     * @throws JsonProcessingException
     */
    @Override
    public SocialUserInfoData getUserInfo(String accessToken) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        String responseBody = webClient.get()
                .uri("/oauth2/v2/userinfo")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("email").asText();
        String providerId = jsonNode.get("id").asText();

        return SocialUserInfoData.create(email, providerId, UserSocialType.GOOGLE);
    }
}
