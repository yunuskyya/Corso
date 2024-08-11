package com.infina.corso.service;

import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse authenticate(CredentialsRequest credentials);

    void logout(String authorizationHeader);

    public int getCurrentUserId();
}
