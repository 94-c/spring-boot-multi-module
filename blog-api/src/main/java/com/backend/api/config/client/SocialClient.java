package com.backend.api.config.client;

import com.backend.core.domain.user.data.SocialUserInfoData;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SocialClient {
    String getAccessToken(String code, String state);
    String getAccessToken(String code) throws JsonProcessingException;
    SocialUserInfoData getUserInfo(String accessToken) throws JsonProcessingException;
}
