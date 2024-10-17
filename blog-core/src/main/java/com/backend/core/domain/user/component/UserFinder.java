package com.backend.core.domain.user.component;

import com.backend.core.domain.user.User;
import com.backend.core.domain.user.exception.UserNotFoundException;
import com.backend.core.domain.user.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;

    /**
     * 이메일을 통해 사용자가 존재하는지 확인
     * @param email 사용자 이메일
     * @return 사용자가 존재하면 true, 존재하지 않으면 false
     */
    public boolean isUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 이메일을 통해 사용자 정보 조회
     * @param email 사용자 이메일
     * @return User 객체 (사용자가 존재하지 않으면 예외 발생)
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    /**
     * 사용자 인덱스를 통해 사용자 정보 조회
     * @param userId 사용자 인덱스
     * @return User 객체 (사용자가 존재하지 않으면 예외 발생)
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
