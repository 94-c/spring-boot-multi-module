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

    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    private String nickname;

    @NotNull(message = "성별을 입력해 주세요.")
    private UserGenderType gender;

    @NotNull(message = "소셜 타입을 입력해 주세요.")
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

    public static UserCreateData of(CreateUserRequest request) {
       return new UserCreateData(
               request.getEmail(),
               request.getNickname(),
               request.getGender(),
               request.getSocial(),
               request.getWeight(),
               request.getHeight(),
               request.getRoles()
       );
    }

}
