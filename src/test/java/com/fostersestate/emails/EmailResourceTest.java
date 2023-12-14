package com.fostersestate.emails;

import jakarta.ws.rs.Path;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
class EmailResourceTest {

    @Test
    void hello() {
        given()
                .when().get("api/email")
                .then()
                .statusCode(200)
                .body(is("Hello from RESTEasy Reactive"));
    }
}