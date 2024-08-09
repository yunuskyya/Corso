package com.infina.corso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\.]).{8,}$", message = "{corso.validation.pattern.password}")
    private String newPassword;
}

