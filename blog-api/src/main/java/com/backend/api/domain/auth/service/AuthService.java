package com.backend.api.domain.auth.service;

import com.backend.api.domain.auth.dto.request.CreateUserRequest;
import com.backend.core.domain.user.component.UserCreator;
import com.backend.core.domain.user.data.UserCreateData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public final UserCreator userCreator;

    public Long create(CreateUserRequest request) {
        UserCreateData data = CreateUserRequest.of(request);
        return userCreator.createUser(data);
    }
}
