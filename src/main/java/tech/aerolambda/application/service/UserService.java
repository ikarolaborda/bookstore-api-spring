package tech.aerolambda.application.service;

import tech.aerolambda.application.dto.user.UserRequest;
import tech.aerolambda.application.dto.user.UserResponse;
import tech.aerolambda.application.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse create(UserRequest request);

    UserResponse findById(Long id);

    UserResponse findByEmail(String email);

    List<UserResponse> findAll();

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);
}
