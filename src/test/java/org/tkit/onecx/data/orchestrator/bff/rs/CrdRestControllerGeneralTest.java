package org.tkit.onecx.data.orchestrator.bff.rs;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jboss.resteasy.reactive.RestResponse.Status.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.tkit.onecx.data.orchestrator.bff.rs.controllers.CrdRestController;

import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.ContextKindDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.CrdSearchCriteriaDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.EditResourceRequestDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;

@WithKubernetesTestServer
@QuarkusTest
@TestHTTPEndpoint(CrdRestController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CrdRestControllerGeneralTest extends AbstractTest {

    @Test
    void edit_without_crds_test() {
        EditResourceRequestDTO editResourceRequestDTO = new EditResourceRequestDTO();

        given()
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .when().body(editResourceRequestDTO).contentType(APPLICATION_JSON)
                .post("/edit").then().statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void edit_without_body_test() {
        given()
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN).when().contentType(APPLICATION_JSON)
                .post("/edit").then().statusCode(BAD_REQUEST.getStatusCode());
    }

    @Test
    void search_no_crds_existing() {
        CrdSearchCriteriaDTO criteriaDTO = new CrdSearchCriteriaDTO();
        criteriaDTO.setType(List.of(ContextKindDTO.DATA));
        criteriaDTO.setName("Not_Existing");
        given()
                .when()
                .contentType(APPLICATION_JSON)
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .body(criteriaDTO)
                .post()
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }
}
