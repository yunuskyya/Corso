package com.infina.corso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerByBrokerResponseTransactionPage {
    private Long id;
    private String name;
    private String surname;
}