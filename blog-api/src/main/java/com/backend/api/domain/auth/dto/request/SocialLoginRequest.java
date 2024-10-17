package com.backend.api.domain.auth.dto.request;

import com.backend.core.domain.user.data.SocialUserInfoData;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SocialLoginRequest {
    private String accessToken;
    private SocialUserInfoData userInfo;
    private CreateUserRequest createUserRequest;
}
