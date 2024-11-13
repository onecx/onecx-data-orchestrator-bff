package org.tkit.onecx.data.orchestrator.bff.rs;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jboss.resteasy.reactive.RestResponse.Status.OK;

import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.tkit.onecx.data.orchestrator.bff.rs.controllers.CrdRestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.*;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;

@WithKubernetesTestServer
@QuarkusTest
@TestHTTPEndpoint(CrdRestController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CrdRestControllerSlotTest extends AbstractTest {

    @Inject
    KubernetesClient client;

    @Inject
    ObjectMapper objectMapper;

    @BeforeAll
    void before() {
        // Creating a custom resource from yaml
        CustomResourceDefinition aCustomResourceDefinition = client.apiextensions().v1().customResourceDefinitions()
                .load(CrdRestControllerSlotTest.class.getResourceAsStream("/mocks/slotDefinition.yml")).item();
        client.apiextensions().v1().customResourceDefinitions().resource(aCustomResourceDefinition).create();
        client.resource(CrdRestControllerSlotTest.class.getResourceAsStream("/mocks/slotMock.yml")).create();
    }

    @Test
    void testInteractionWithAPIServer() {
        CrdSearchCriteriaDTO criteriaDTO = new CrdSearchCriteriaDTO();
        criteriaDTO.setName("onecx-workspace-ui-onecx-avatar-image");
        criteriaDTO.setType(List.of(ContextKindDTO.SLOT));
        var response = given()
                .when()
                .contentType(APPLICATION_JSON)
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .body(criteriaDTO)
                .post()
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(OK.getStatusCode())
                .extract().as(CrdResponseDTO.class);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getCustomResources().get(0).getName());
    }

    @Test
    void testEditResource() {
        var response = given()
                .when()
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .pathParam("type", ContextKindDTO.SLOT)
                .pathParam("name", "onecx-workspace-ui-onecx-avatar-image")
                .get("/{type}/{name}")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(OK.getStatusCode())
                .extract().as(GetCRDResponseDTO.class);
        Assertions.assertNotNull(response);
        CustomResourceSlotDTO editedData = objectMapper.convertValue(response.getCrd(), CustomResourceSlotDTO.class);
        editedData.getSpec().setAppId("EditedAppId");
        EditResourceRequestDTO editResourceRequestDTO = new EditResourceRequestDTO();
        editResourceRequestDTO.setCrdSlot(editedData);

        given().when()
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .body(editResourceRequestDTO).contentType(APPLICATION_JSON)
                .post("/edit").then().statusCode(OK.getStatusCode());

        response = given()
                .when()
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .pathParam("type", ContextKindDTO.SLOT)
                .pathParam("name", "onecx-workspace-ui-onecx-avatar-image")
                .get("/{type}/{name}")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(OK.getStatusCode())
                .extract().as(GetCRDResponseDTO.class);
        editedData = objectMapper.convertValue(response.getCrd(), CustomResourceSlotDTO.class);
        Assertions.assertNotNull(editedData);
        Assertions.assertEquals("EditedAppId", editedData.getSpec().getAppId());
    }
}
