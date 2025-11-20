package org.tkit.onecx.data.orchestrator.bff.rs;

import jakarta.inject.Inject;

import org.junit.jupiter.api.*;
import org.tkit.onecx.data.orchestrator.bff.rs.controllers.CrdRestController;

import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;

@WithKubernetesTestServer
@QuarkusTest
@TestHTTPEndpoint(CrdRestController.class)
class CrdRestControllerExceptionTest extends AbstractTest {

    @Inject
    CrdRestController restController;

    @Test
    void kubernetes_client_exception_test() {
        Status status = new Status();
        status.setStatus("error");
        status.setCode(400);
        status.setMessage("some error");
        var response = restController.kubernetesException(new KubernetesClientException(status));
        Assertions.assertEquals(400, response.getStatus(), "expected HTTP 400 status");

    }
}
