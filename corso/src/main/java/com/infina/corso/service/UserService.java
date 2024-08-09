package com.infina.corso.service;

import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<GetAllUserResponse> getAllUser(Pageable pageable);
    void registerBroker(RegisterUserRequest registerUserRequest);
    void registerManager(RegisterUserRequest registerUserRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    void activateUserByEmail(String email);
    void deleteUser(int id);
}
