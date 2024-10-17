package com.backend.api.config.client.dto;

import com.backend.core.domain.user.type.UserSocialType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SocialUserInfoData {
    private String email;
    private String providerId;
    private UserSocialType socialType;

    public SocialUserInfoData(String email, String providerId, UserSocialType socialType) {
        this.email = email;
        this.providerId = providerId;
        this.socialType = socialType;
    }

    public static SocialUserInfoData create(String email, String providerId, UserSocialType socialType) {
        return new SocialUserInfoData(email, providerId, socialType);
    }
}
