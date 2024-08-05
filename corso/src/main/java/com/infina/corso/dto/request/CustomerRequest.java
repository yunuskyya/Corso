package com.infina.corso.dto.request;

import com.infina.corso.model.enums.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private CustomerType customerType;
    private String tcKimlikNo;
    private String companyName;
    private String vkn;
    @Pattern(regexp = "^(05)[0-9]{9}$") // 05XX XXX XX XX // arada boşluk olmadan
    private String phone;
    @Email
    private String email;
    private Long userId; // hangi broker'ın customer'ı
}
