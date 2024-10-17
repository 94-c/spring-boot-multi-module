package com.backend.api.domain.auth.service;

import com.backend.api.config.client.GoogleClient;
import com.backend.api.config.client.KakaoClient;
import com.backend.api.config.client.NaverClient;
import com.backend.core.domain.user.data.SocialUserInfoData;
import com.backend.api.domain.auth.dto.request.CreateUserRequest;
import com.backend.api.domain.auth.jwt.JwtTokenProvider;
import com.backend.core.domain.user.User;
import com.backend.core.domain.user.component.UserCreator;
import com.backend.core.domain.user.component.UserFinder;
import com.backend.core.domain.user.data.UserCreateData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;
    private final GoogleClient googleClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserCreator userCreator;
    private final UserFinder userFinder;

    /**
     * provider에 따른 인증 URL을 생성하여 반환
     * @param provider 소셜 로그인 제공자 (kakao, naver, google)
     * @return 인증 URL
     */
    public String getAuthUrl(String provider) {
        switch (provider.toLowerCase()) {
            case "kakao":
                return kakaoClient.getAuthUrl();
            case "naver":
                return naverClient.getAuthUrl();
            case "google":
                return googleClient.getAuthUrl();
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 제공자입니다: " + provider);
        }
    }

    /**
     * 회원가입 처리 (소셜 로그인 정보를 포함한 추가 정보를 통해 가입)
     * @param userInfo 소셜 로그인 사용자 정보
     * @param request 추가 사용자 정보 (닉네임, 성별, 키, 몸무게)
     * @return 생성된 사용자 ID
     */
    public Long registerUserWithAdditionalInfo(SocialUserInfoData userInfo, CreateUserRequest request) {
        UserCreateData userCreateData = convertToUserCreateData(userInfo, request);
        return userCreator.createUserFromSocial(userCreateData);
    }
    /**
     * JWT 토큰 발급
     * @param user 사용자 정보
     * @return 생성된 JWT 토큰
     */
    public String generateJwtToken(User user) {
        // 사용자 객체에서 필요한 정보로 JWT 토큰 생성
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

    /**
     * 회원 여부 확인
     * @param userInfo 소셜 사용자 정보
     * @return 회원 존재 여부 (true = 회원 존재, false = 회원 없음)
     */
    public boolean isUserExists(SocialUserInfoData userInfo) {
        return userFinder.isUserExistsByEmail(userInfo.getEmail());
    }

    /**
     * 사용자 ID로 사용자 조회
     * @param userId 사용자 ID
     * @return 조회된 사용자
     */
    public User findUserById(Long userId) {
        return userFinder.findUserById(userId);
    }

    /**
     * 이메일로 사용자 정보 조회
     * @param email 사용자 이메일
     * @return User 객체
     */
    public User findUserByEmail(String email) {
        return userFinder.findUserByEmail(email);
    }

    /**
     * CreateUserRequest와 SocialUserInfoData를 UserCreateData로 변환
     * @param userInfo 소셜 사용자 정보
     * @param request 사용자가 입력한 추가 정보
     * @return 변환된 UserCreateData 객체
     */
    private UserCreateData convertToUserCreateData(SocialUserInfoData userInfo, CreateUserRequest request) {
        return new UserCreateData(
                userInfo.getEmail(),
                request.getNickname(),
                request.getGender(),
                userInfo.getSocialType(),
                request.getWeight(),
                request.getHeight(),
                request.getRoles()
        );
    }

}
