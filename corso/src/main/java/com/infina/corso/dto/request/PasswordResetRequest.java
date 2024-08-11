package com.infina.corso.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotEmpty
    @NotBlank
    @Email
    private String email;
}
