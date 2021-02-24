package com.hmrc.integrations.api.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmrc.integrations.api.model.request.EmployeeRequest;
import com.hmrc.integrations.api.model.response.EmployeeResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class EmployeeProcessor implements Processor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void process(Exchange exchange) throws Exception {
        EmployeeRequest request = objectMapper.readValue(exchange.getIn().getBody(String.class),
                EmployeeRequest.class);
        exchange.getIn().setBody(objectMapper.writeValueAsString(calculateBonus(request)));
    }

    public EmployeeResponse calculateBonus(EmployeeRequest employee) {
        return new EmployeeResponse(
                employee.getEmpId(),
                "Hello, " + employee.getFullName(),
                employee.getSalary(),
                employee.getSalary().multiply(BigDecimal.valueOf(0.12))
        );
    }
}
