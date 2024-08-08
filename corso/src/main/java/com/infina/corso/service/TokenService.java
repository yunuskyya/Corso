package com.infina.corso.service;

import com.infina.corso.dto.request.CredentialsRequest;
import com.infina.corso.dto.response.GetUserByEmailResponse;
import com.infina.corso.model.Token;
import com.infina.corso.model.User;

public interface TokenService {
    public Token generateToken(User userInDB);
    public User verifyToken(String authorizationHeader);

    public void logout(String authorizationHeader);

    public Token findToken(String cookieValue);

    public void updateExpirationDate(Token token);

    public void updateTokenUser(int userId);

    public Token findTokenByUserId(int userId);

}
