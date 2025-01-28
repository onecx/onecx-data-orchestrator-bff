package org.tkit.onecx.data.orchestrator.bff.rs;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * This test is required to initialize standard tests from libraries
 */
@QuarkusTest
public class BaseFirstTest extends AbstractTest {

    @Test
    void testQuarkusCloud() {
        given()
                .when()
                .get("/q/metrics")
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/q/health")
                .then()
                .statusCode(200);
    }
}
