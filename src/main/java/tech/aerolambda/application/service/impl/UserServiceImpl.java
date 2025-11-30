package tech.aerolambda.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.aerolambda.application.dto.user.UserRequest;
import tech.aerolambda.application.dto.user.UserResponse;
import tech.aerolambda.application.dto.user.UserUpdateRequest;
import tech.aerolambda.application.mapper.UserMapper;
import tech.aerolambda.application.service.UserService;
import tech.aerolambda.domain.entity.User;
import tech.aerolambda.domain.repository.UserRepository;
import tech.aerolambda.infrastructure.exception.DuplicateResourceException;
import tech.aerolambda.infrastructure.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (request.email() != null && !user.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("User", "email", request.email());
        }

        userMapper.updateEntity(request, user);

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
