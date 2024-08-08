package com.infina.corso.dto.response;

import com.infina.corso.model.Account;
import com.infina.corso.model.enums.CustomerStatus;
import com.infina.corso.model.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerFilterResponse {
    private Long id;
    private String name; // name + surname or companyName
    private String tcKimlikNo;
    private String vkn;
    private CustomerType customerType;
    private CustomerStatus status;
    private String phone;
    private String email;
    private List<AccountResponse> accounts;
    private LocalDateTime createdAt;
}
