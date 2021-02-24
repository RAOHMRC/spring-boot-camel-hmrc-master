package com.hmrc.integrations.api.test;

import com.hmrc.integrations.api.model.request.EmployeeRequest;
import com.hmrc.integrations.api.model.response.EmployeeResponse;
import com.hmrc.integrations.api.processors.EmployeeProcessor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class EmployeeProcessorTest {

    @Test
    public void whenSalaryProvided_thenReturnBonus() {
        EmployeeProcessor employeeProcessor = new EmployeeProcessor();
        EmployeeRequest employeeRequest = new EmployeeRequest(1, "TestEmployee", new BigDecimal(1000));
        EmployeeResponse employeeResponse = employeeProcessor.calculateBonus(employeeRequest);
        Assert.assertEquals(employeeResponse.getBonus().longValue(), new BigDecimal(120).longValue());
    }

}
