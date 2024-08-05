package com.infina.corso.service;

import com.infina.corso.dto.request.ChangePasswordRequest;
import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;

import java.util.List;

public interface UserService {
   public List<GetAllUserResponse> getAllUser();
   public void registerBroker(RegisterUserRequest registerUserRequest);
   public void registerManager(RegisterUserRequest registerUserRequest);
   void changePassword(ChangePasswordRequest changePasswordRequest);
   void activateUserByEmail(String email);
}
