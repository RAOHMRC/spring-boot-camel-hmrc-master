## Spring Boot Camel

This module contains API for integrating Spring Boot with Apache Camel. 

### JSON Schemas

The schemas used for validating request JSON and response JSON are stored under resources folder.

### How to run

To start the application, run below command from project folder

`mvn spring-boot:run`
	
Then, make a POST http request to:

`http://localhost:8080/camel/api/bean` 

Change the HEADER: Content-Type: application/json, 

and a BODY payload like:

`{"empId": 1, "fullName": "Rao", "salary" : 10000}`

Submit the request

### Functionality

##### Positive Test
 
Once request is submitted, the request message will be validated against the schema InputSchema.json. If the JSON is not complaint with JSON Schema then a response with detailed list of errrors is generated and sent back to the client.

If the content of JSON message is valid then the bonus for that employee is calculated based on the salary and JSON response is generated.
 
The generated response is validated against another schema OutputSchema.json, if there are any validation issues, an error list is populated in a JSON and returned with HTTP Status Code 500.

If validation passes, the response message along with calculated bonus attribute is passed back to client with HTTP Status code 200.

The postman collection is under src/misc folder.

##### Negative Test

For a negative test, user can remove any one of the attributes in the request JSON. The validation will kick in and sends response with validation errors. 

### Docker

There is a docker file in the root folder which can be used to build image and run 
1. To Build a docker image run below command<br />
	docker build --tag springbootcamel:1.0 .

2. To run the container, run below command and then postman can be used to send requests<br />
	docker run --publish 8080:8080 --detach --name sbci springbootcamel:1.0

### Testing

The HMRCIntegrationsAPITest class contains the automated tests for positive and negative scenarios.

NOTE: One of the requirements is to write a mock service to return a response. 
This can be done by using WireMock. I have created a separate test WiremockJunitLiveTest which stubs the response operation and the end point tested in the same method. 