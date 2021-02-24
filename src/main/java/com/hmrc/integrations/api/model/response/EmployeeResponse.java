package com.hmrc.integrations.api.model.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Integer empId;
    private String fullName;
    private BigDecimal salary;
    private BigDecimal bonus;
}
