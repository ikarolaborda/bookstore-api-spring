package tech.aerolambda.application.service;

import tech.aerolambda.application.dto.auth.AuthResponse;
import tech.aerolambda.application.dto.auth.LoginRequest;
import tech.aerolambda.application.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String token);

    void logout(String token);
}
