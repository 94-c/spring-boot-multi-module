package com.backend.api.domain.auth.jwt;

import com.backend.core.domain.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    /**
     * JWT 토큰 생성
     * @param email 사용자 이메일
     * @param roles 사용자 권한 목록
     * @return 생성된 JWT 토큰
     */
    public String createToken(String email, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles.stream().map(Role::getType).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
