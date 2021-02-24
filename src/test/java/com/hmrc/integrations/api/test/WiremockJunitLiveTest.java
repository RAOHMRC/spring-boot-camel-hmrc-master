package com.hmrc.integrations.api.test;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.hmrc.integrations.api.model.request.EmployeeRequest;
import org.json.JSONException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WiremockJunitLiveTest {

    private static final String URL = "/camel/api/bean";

    private WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8888));

    @Test
    public void givenProgrammaticallyManagedServer_whenUsingSimpleStubbing_thenCorrect() throws URISyntaxException, JSONException {
        wireMockServer.start();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        configureFor("localhost", 8888);

        String expectedJson = "{\r\n" +
                "    \"empId\": 1000,\r\n" +
                "    \"fullName\": \"Hello, Rao\",\r\n" +
                "    \"salary\": 10000,\r\n" +
                "    \"bonus\": 1200.0\r\n" +
                "}";

        stubFor(post(urlEqualTo(URL)).willReturn(aResponse().withBody(expectedJson)));

        URI url = new URI("http://localhost:8888/camel/api/bean");
        EmployeeRequest objEmp = new EmployeeRequest(1000, "Rao", BigDecimal.valueOf(10000));
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(objEmp, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JSONAssert.assertEquals(expectedJson, responseEntity.getBody(), false);
        wireMockServer.stop();
    }
}