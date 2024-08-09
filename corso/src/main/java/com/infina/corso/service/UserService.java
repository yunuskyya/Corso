package com.infina.corso.service;

import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;

import java.util.List;

public interface UserService {
    List<GetAllUserResponse> getAllUser();
    void registerBroker(RegisterUserRequest registerUserRequest);
    void registerManager(RegisterUserRequest registerUserRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    void activateUserByEmail(String email);
    void deleteUser(int id);
}
