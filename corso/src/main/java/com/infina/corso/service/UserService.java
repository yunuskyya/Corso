package com.infina.corso.service;

import com.infina.corso.dto.request.*;
import com.infina.corso.dto.response.GetAllUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<GetAllUserResponse> getAllUser(Pageable pageable);
    void registerBroker(RegisterUserRequest registerUserRequest);
    void registerManager(RegisterManagerRequest request);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    void deleteUser(int id);
    void updatePassword(String token, UpdatePasswordRequest request);
    void resetPassword(PasswordResetRequest passwordResetRequest);
    void userUnblock(UserUnblockRequest userUnblockRequest);
    void activateUser(UserActivateRequest userActivateRequest);
}
