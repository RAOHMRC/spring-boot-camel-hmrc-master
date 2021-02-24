package com.hmrc.integrations.api.model.request;

import lombok.*;

import java.math.BigDecimal;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private Integer empId;
    private String fullName;
    private BigDecimal salary;
}
