package com.backend.api.config.client;

import com.backend.core.domain.user.data.SocialUserInfoData;
import com.backend.core.domain.user.type.UserSocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoClient implements SocialClient {
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public KakaoClient(@Value("${spring.kakao.client_id}") String clientId,
                       @Value("${spring.kakao.redirect_uri}") String redirectUri,
                       @Value("${spring.kakao.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    /**
     * 카카오는 state 파라미터를 지원하지 않으므로 해당 메서드는 지원되지 않음
     */
    @Override
    public String getAccessToken(String code, String state) {
        throw new UnsupportedOperationException("카카오는 state를 지원하지 않습니다.");
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
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        String responseBody = webClient.post()
                .uri("/oauth/token")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Invalid request")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server error")))
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    /**
     * accessToken을 사용하여 카카오 사용자 정보를 조회하여 반환
     * @param accessToken
     * @return SocialUserInfoData
     * @throws JsonProcessingException
     */
    @Override
    public SocialUserInfoData getUserInfo(String accessToken) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        String responseBody = webClient.get()
                .uri("/v2/user/me")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Invalid request")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server error")))
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String providerId = jsonNode.get("id").asText();

        return SocialUserInfoData.create(email, providerId, UserSocialType.KAKAO);
    }
}
