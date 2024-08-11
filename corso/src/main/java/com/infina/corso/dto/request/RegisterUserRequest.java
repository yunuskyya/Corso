package com.infina.corso.dto.request;

import com.infina.corso.validation.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {
    @Size(min = 3, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.firstName}")
    private String firstName;

    @Size(min = 3, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.lastName}")
    private String lastName;

    @Size(min = 5, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.username}")
    private String username;

    @Size(min = 5, max = 50)
    @NotBlank
    private String phone;

    @Size(min = 8, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.email}")
    @Email(message = "{corso.validation.email}")
    @UniqueEmail
    private String email;
}
