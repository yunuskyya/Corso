package com.infina.corso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterUserRequest {
    @Size(min = 3, max = 50)
    @NotBlank
    private String firstName;
    @Size(min = 3, max = 50)
    @NotBlank
    private String lastName;
    @Size(min = 3, max = 50)
    @NotBlank
    private String username;
    @Size(min = 3, max = 50)
    @NotBlank
    private String email;
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\.]).{8,}$",message = "corso.validation.pattern.password")
    private String password;
}