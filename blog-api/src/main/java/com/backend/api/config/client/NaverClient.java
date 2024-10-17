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
public class NaverClient implements SocialClient {
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public NaverClient(@Value("${spring.naver.client_id}") String clientId,
                       @Value("${spring.naver.redirect_uri}") String redirectUri,
                       @Value("${spring.naver.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
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


    @Override
    public String getAccessToken(String code, String state) {
        throw new UnsupportedOperationException("네이버는 state를 지원하지 않습니다.");
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
                .baseUrl("https://nid.naver.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        String responseBody = webClient.post()
                .uri("/oauth2.0/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    /**
     * accessToken을 사용하여 Naver 사용자 정보를 조회하여 반환
     * @param accessToken
     * @return SocialUserInfoData
     * @throws JsonProcessingException
     */
    @Override
    public SocialUserInfoData getUserInfo(String accessToken) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        String responseBody = webClient.get()
                .uri("/v1/nid/me")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("response").get("email").asText();
        String providerId = jsonNode.get("response").get("id").asText();

        return SocialUserInfoData.create(email, providerId, UserSocialType.NAVER);
    }
}
