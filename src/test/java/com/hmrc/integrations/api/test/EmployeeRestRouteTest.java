
package com.hmrc.integrations.api.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmrc.integrations.api.Application;
import com.hmrc.integrations.api.model.request.EmployeeRequest;
import com.hmrc.integrations.api.model.response.EmployeeResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class EmployeeRestRouteTest {


    private static HttpHeaders headers;

    @Autowired
    ObjectMapper objectMapper;


    @BeforeClass
    public static void runBeforeAllTestMethods() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

    }

    @Test
    public void shouldCalculateEmployeeBonus() throws URISyntaxException {
        URI url = new URI("http://localhost:8082/camel/api/bean");
        EmployeeRequest objEmp = new EmployeeRequest(1000, "Rao Konda", BigDecimal.valueOf(10000));
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(objEmp, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EmployeeResponse> responseEntity = restTemplate.postForEntity(url, requestEntity, EmployeeResponse.class);
        EmployeeResponse employeeResponse = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(employeeResponse);
        assertEquals("1200.00", employeeResponse.getBonus().toString());
    }


    @Test(expected = HttpServerErrorException.InternalServerError.class)
    public void shouldReturnValidationExceptionForInvalidEmployeeRequest() throws URISyntaxException {
        URI url = new URI("http://localhost:8082/camel/api/bean");
        EmployeeRequest empObj = new EmployeeRequest();
        empObj.setEmpId(123);
        empObj.setSalary(BigDecimal.valueOf(10000));
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(empObj, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
