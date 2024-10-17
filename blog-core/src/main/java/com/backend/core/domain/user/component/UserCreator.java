package com.backend.core.domain.user.component;

import com.backend.core.domain.user.data.SocialUserInfoData;
import com.backend.core.domain.user.User;
import com.backend.core.domain.user.data.UserCreateData;
import com.backend.core.domain.user.exception.UserEmailDuplicationException;
import com.backend.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserRepository userRepository;

    /**
     * 소셜 로그인 사용자 정보를 기반으로 회원가입 처리
     * @param data 회원가입 시 사용되는 추가적인 사용자 정보 (닉네임, 성별, 키, 몸무게 등)
     * @return 생성된 사용자의 ID
     */
    public Long createUserFromSocial(UserCreateData data) {
        if (userRepository.existsByEmail(data.getEmail())) {
            throw new UserEmailDuplicationException(HttpStatus.CONFLICT, "중복되는 이메일이 존재합니다.");
        }

        // 새로운 사용자 생성 및 저장
        User user = User.fromCreateData(data);
        return userRepository.save(user).getId();
    }
}
