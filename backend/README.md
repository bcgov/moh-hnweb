# HNWeb API

HNWeb API is the API backend for [HN Web](../frontend). It handles communication with MSP Direct (via JMB?) and E45/R15/R50 Web Services.

# Prerequisites

Tested with:
* Java 11
* Maven 3.6.3

# Run

To start the application run:
```
./mvnw spring-boot:run
```

To create an executable JAR run:
```
mvn clean package
```

To run the JAR:
```
java -jar target/<jar-file-name>.jar
```

To run using docker:
```
docker build -t hnweb-api .
docker run -p:9091:9090 hnweb-api
```

# Swagger/OpenAPI
Swagger/OpenAPI documentation is available at:
* http://localhost:9090/docs/swagger-ui.html (Swagger)
* http://localhost:9090/docs/api-docs (OpenAPI)

# Verification
The skeleton application can be tested by sending a request to:
```
GET http://localhost:9090/r41?phn=12345&phn=56789
```
You will need a valid access token from https://common-logon-dev.hlth.gov.bc.ca/auth/realms/v2_pos/protocol/openid-connect/token to access this endpoint.
If you want to test without authentication required, just modify SecurityConfig.java as follows:
```
//.mvcMatchers(HttpMethod.GET,"/r41/**").hasRole("manage-clients")
.mvcMatchers(HttpMethod.GET, "/r41/**").permitAll()
```


# Logging
Application logs are logged to the console and logs/hnweb-api.log.