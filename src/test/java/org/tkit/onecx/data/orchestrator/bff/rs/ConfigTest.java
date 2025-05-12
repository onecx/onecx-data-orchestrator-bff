package org.tkit.onecx.data.orchestrator.bff.rs;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jboss.resteasy.reactive.RestResponse.Status.OK;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;

import org.eclipse.microprofile.config.Config;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.tkit.onecx.data.orchestrator.bff.rs.configs.DataOrchestratorConfig;
import org.tkit.onecx.data.orchestrator.bff.rs.controllers.CrdRestController;

import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.ContextKindDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.CrdResponseDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.CrdSearchCriteriaDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.GetContextKindsResponseDTO;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;
import io.smallrye.config.SmallRyeConfig;

@WithKubernetesTestServer
@QuarkusTest
@TestHTTPEndpoint(CrdRestController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigTest extends AbstractTest {

    @Inject
    KubernetesClient client;

    @InjectMock
    DataOrchestratorConfig dataConfig;

    public static class ConfigProducer {

        @Inject
        Config config;

        @Produces
        @ApplicationScoped
        @Mock
        DataOrchestratorConfig config() {
            return config.unwrap(SmallRyeConfig.class).getConfigMapping(DataOrchestratorConfig.class);
        }
    }

    @BeforeEach
    void beforeEach() {
        Mockito.when(dataConfig.types().get("database")).thenReturn(new DataOrchestratorConfig.TypeConfig() {
            @Override
            public boolean enabled() {
                return false;
            }

            @Override
            public String value() {
                return "Database";
            }
        });
        Mockito.when(dataConfig.types().get("microfrontend")).thenReturn(new DataOrchestratorConfig.TypeConfig() {
            @Override
            public boolean enabled() {
                return true;
            }

            @Override
            public String value() {
                return "Microfrontend";
            }
        });
    }

    @BeforeAll
    void before() {
        // Creating a custom resource from yaml
        CustomResourceDefinition aCustomResourceDefinition = client.apiextensions().v1().customResourceDefinitions()
                .load(CrdRestControllerDatabaseTest.class.getResourceAsStream("/mocks/databaseDefinition.yml")).item();
        client.apiextensions().v1().customResourceDefinitions().resource(aCustomResourceDefinition).create();
        client.resource(CrdRestControllerDatabaseTest.class.getResourceAsStream("/mocks/databaseMock.yml")).create();
    }

    @Test
    void testInteractionWithAPIServer() {
        CrdSearchCriteriaDTO criteriaDTO = new CrdSearchCriteriaDTO();
        criteriaDTO.setName("onecx-help-svc-db");
        criteriaDTO.setType(List.of(ContextKindDTO.DATABASE));
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
        Assertions.assertEquals(0, response.getCustomResources().size());
    }

    @Test
    void getActiveCrdsbyConfig() {
        var response = given()
                .when()
                .contentType(APPLICATION_JSON)
                .auth().oauth2(keycloakClient.getAccessToken(ADMIN))
                .header(APM_HEADER_PARAM, ADMIN)
                .get("/kinds")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(OK.getStatusCode())
                .extract().as(GetContextKindsResponseDTO.class);

        Assertions.assertEquals(ContextKindDTO.MICROFRONTEND, response.getKinds().get(0));
    }
}
