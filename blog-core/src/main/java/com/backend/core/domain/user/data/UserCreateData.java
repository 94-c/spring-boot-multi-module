package com.backend.core.domain.user.data;

import com.backend.core.domain.user.Role;
import com.backend.core.domain.user.type.UserGenderType;
import com.backend.core.domain.user.type.UserSocialType;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserCreateData {
    private final String email;
    private final String nickname;
    private final UserGenderType gender;
    private final UserSocialType social;
    private final Double weight;
    private final Double height;
    private final Set<Role> roles;

    public UserCreateData(String email, String nickname, UserGenderType gender, UserSocialType social, Double weight, Double height, Set<Role> roles) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.social = social;
        this.weight = weight;
        this.height = height;
        this.roles = roles;
    }

}
