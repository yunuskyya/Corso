package com.infina.corso.dto.response;

import com.infina.corso.model.enums.CustomerStatus;
import com.infina.corso.model.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerGetByIdResponse {
    private Long id;
    private String name;
    private String surname;
    private String tcKimlikNo;
    private String companyName;
    private String vkn;
    private CustomerType customerType;
    private CustomerStatus status;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
