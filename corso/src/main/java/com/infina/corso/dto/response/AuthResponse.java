package com.infina.corso.dto.response;

import com.infina.corso.model.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {

    GetUserByEmailResponse user;
    Token token;

}

