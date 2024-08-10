package com.infina.corso.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserActivateRequest {
    @NotNull
    @Email
    private String email;
}