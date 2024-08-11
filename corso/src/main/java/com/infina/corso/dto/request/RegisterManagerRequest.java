package com.infina.corso.dto.request;

import com.infina.corso.validation.annotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterManagerRequest {
    @Size(min = 3, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.firstName}")
    private String firstName;

    @Size(min = 3, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.lastName}")
    private String lastName;

    @Size(min = 3, max = 50)
    @NotBlank(message = "{corso.validation.notBlank.email}")
    @Email(message = "{corso.validation.email}")
    @UniqueEmail
    private String email;

    @Size(min = 5, max = 50, message = "{corso.validation.size.password}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\.]).{8,}$", message = "{corso.validation.pattern.password}")
    private String password;
}