package com.backend.api.domain.auth.dto.response;

import com.backend.core.domain.user.type.UserSocialType;
import lombok.Getter;

@Getter
public class LoginResponse {
    private String jwtToken;
    private String email;
    private String nickname;
    private UserSocialType socialType;

    public LoginResponse(String jwtToken, String email, String nickname, UserSocialType socialType) {
        this.jwtToken = jwtToken;
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
    }
}
