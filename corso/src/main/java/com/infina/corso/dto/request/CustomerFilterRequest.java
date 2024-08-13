package com.infina.corso.dto.request;

import com.infina.corso.model.enums.CustomerStatus;
import com.infina.corso.model.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerFilterRequest {
    private Long userId; // brokerId
    private Long customerId;
    private Long accountId; // accountNumber that customer has
    private String name; // name + surname or companyName
    private String tcKimlikNo;
    private String vkn;
    private CustomerType customerType;
    private CustomerStatus status;
    private String currencyCode;
    private String phone;
    private String email;
    private LocalDate dateStart;
    private LocalDate dateEnd;
}
