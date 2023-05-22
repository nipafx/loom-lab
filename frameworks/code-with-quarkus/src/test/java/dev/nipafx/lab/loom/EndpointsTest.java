package dev.nipafx.lab.loom;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class EndpointsTest {

    @Test
    public void testEndpoints() {
        given()
          .when().get("/api/current-thread")
          .then()
             .statusCode(200)
             .body(notNullValue());
    }

}