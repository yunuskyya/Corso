package com.infina.corso.service;

import com.infina.corso.model.Token;
import com.infina.corso.model.User;

public interface TokenService {
    Token generateToken(User userInDB);
    User verifyToken(String authorizationHeader);
    Token findToken(String cookieValue);
    void updateExpirationDate(Token token);
    void updateTokenUser(int userId);
    Token findTokenByUserId(int userId);
    void logout(String authorizationHeader);
}