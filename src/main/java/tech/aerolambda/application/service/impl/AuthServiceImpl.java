package tech.aerolambda.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.application.dto.auth.AuthResponse;
import tech.aerolambda.application.dto.auth.LoginRequest;
import tech.aerolambda.application.dto.auth.RegisterRequest;
import tech.aerolambda.application.dto.user.UserResponse;
import tech.aerolambda.application.mapper.UserMapper;
import tech.aerolambda.application.service.AuthService;
import tech.aerolambda.domain.entity.User;
import tech.aerolambda.domain.enums.UserRole;
import tech.aerolambda.domain.repository.UserRepository;
import tech.aerolambda.infrastructure.exception.DuplicateResourceException;
import tech.aerolambda.infrastructure.exception.InvalidCredentialsException;
import tech.aerolambda.infrastructure.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        UserResponse userResponse = userMapper.toResponse(savedUser);

        return new AuthResponse(token, jwtService.getExpirationTime(), userResponse);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return new AuthResponse(token, jwtService.getExpirationTime(), userResponse);
    }

    @Override
    public AuthResponse refresh(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(InvalidCredentialsException::new);

        String newToken = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return new AuthResponse(newToken, jwtService.getExpirationTime(), userResponse);
    }

    @Override
    public void logout(String token) {
        // JWT is stateless - client should discard the token
        // Optionally implement token blacklisting here
    }
}
