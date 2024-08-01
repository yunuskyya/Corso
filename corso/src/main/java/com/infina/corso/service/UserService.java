package com.infina.corso.service;

import com.infina.corso.dto.request.RegisterUserRequest;
import com.infina.corso.dto.response.GetAllUserResponse;

import java.util.List;

public interface UserService {
   public List<GetAllUserResponse> getAllUser();
   public void registerBroker(RegisterUserRequest registerUserRequest);
   public void registerManager(RegisterUserRequest registerUserRequest);
}
