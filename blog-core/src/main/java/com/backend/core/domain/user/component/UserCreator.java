package com.backend.core.domain.user.component;

import com.backend.core.domain.user.User;
import com.backend.core.domain.user.data.UserCreateData;
import com.backend.core.domain.user.exception.UserEmailDuplicationException;
import com.backend.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreator {
    private final UserRepository userRepository;

    public Long createUser(UserCreateData data) {
        boolean result = userRepository.existsByEmail(data.getEmail());
        if (result) {
            throw new UserEmailDuplicationException(HttpStatus.NOT_FOUND, "중복되는 이메일이 존재합니다.");
        }

        User user = User.fromCreateData(data);

        return userRepository.save(user).getId();
    }

}
