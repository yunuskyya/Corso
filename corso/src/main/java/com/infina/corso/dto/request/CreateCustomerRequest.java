package com.infina.corso.dto.request;

import com.infina.corso.model.enums.CustomerType;
import com.infina.corso.validation.annotations.ValidCreateCustomerRequest;
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
@ValidCreateCustomerRequest
public class CreateCustomerRequest {
    private String name;

    private String surname;

    @NotNull
    private CustomerType customerType;
    private String tcKimlikNo;
    private String companyName;
    private String vkn;

    @Pattern(regexp = "^(05)[0-9]{9}$") @NotNull // 05XX XXX XX XX // arada boşluk olmadan
    private String phone;

    @NotNull @Email
    private String email;
}
