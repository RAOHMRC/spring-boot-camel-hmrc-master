package com.hmrc.integrations.api.routebuilders;

import com.hmrc.integrations.api.errorhandlers.GenerateFailureResponse;
import com.hmrc.integrations.api.processors.EmployeeProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
class RestApiRouteBuilder extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    @Value("${context.path}")
    String contextPath;

    @Autowired
    GenerateFailureResponse generateFailureResponse;

    @Autowired
    EmployeeProcessor employeeProcessor;

    @Override
    public void configure() {
        onException(JsonValidationException.class).handled(true).process(generateFailureResponse);
        restConfiguration().contextPath(contextPath)
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/api/").description("Integrations REST API Service")
                .id("api-route")
                .post("/bean")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .bindingMode(RestBindingMode.off)
                .enableCORS(true).route()
                .to("json-validator:employee-request-schema.json")
                .to("direct:employeeService");

        from("direct:employeeService")
                .process(employeeProcessor)
                .to("json-validator:ewmployee-response-schema.json")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));
    }
}