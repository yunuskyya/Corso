package com.infina.corso.dto.response;

import com.infina.corso.model.enums.CustomerType;
import lombok.Data;



@Data
public class GetAllCustomerForEndOfDayResponse {
    private String customerNameSurname;
    private String tcKimlikNo;
    private CustomerType customerType;
    private String companyName;
    private String vkn;
    private String email;

}
