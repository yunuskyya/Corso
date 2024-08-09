package com.infina.corso.dto.request;

import com.infina.corso.model.enums.CustomerStatus;
import com.infina.corso.model.enums.CustomerType;
import com.infina.corso.validation.annotations.ValidCustomerUpdateRequest;
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
@ValidCustomerUpdateRequest
public class CustomerUpdateRequest {
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @NotNull
    private CustomerType customerType;
    @NotNull
    private CustomerStatus status;

    private String tcKimlikNo;
    private String companyName;
    private String vkn;

    @Pattern(regexp = "^(05)[0-9]{9}$") // 05XX XXX XX XX // arada boşluk olmadan
    private String phone;

    @NotNull @Email
    private String email;
}
