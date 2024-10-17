package com.backend.api.domain.auth.dto.request;

import com.backend.core.domain.user.Role;
import com.backend.core.domain.user.data.UserCreateData;
import com.backend.core.domain.user.type.UserGenderType;
import com.backend.core.domain.user.type.UserSocialType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class CreateUserRequest {

    private String email;
    private String nickname;
    private UserGenderType gender;
    private UserSocialType social;
    private Double weight;
    private Double height;
    private Set<Role> roles;

    public CreateUserRequest(String email, String nickname, UserGenderType gender, UserSocialType social, Double weight, Double height, Set<Role> roles) {
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.social = social;
        this.weight = weight;
        this.height = height;
        this.roles = roles;
    }


}
