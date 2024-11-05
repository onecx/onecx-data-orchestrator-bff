package org.tkit.onecx.data.orchestrator.bff.rs.controllers;

import java.util.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.tkit.onecx.data.orchestrator.bff.rs.mappers.CrdMapper;
import org.tkit.onecx.data.orchestrator.bff.rs.mappers.ExceptionMapper;
import org.tkit.quarkus.log.cdi.LogService;

import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.DataApiService;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.CrdResponseDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.EditResourceRequestDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.ProblemDetailResponseDTO;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;

@ApplicationScoped
@Transactional(value = Transactional.TxType.NOT_SUPPORTED)
@LogService
public class CrdRestController implements DataApiService {
    @Inject
    ExceptionMapper exceptionMapper;

    @Inject
    KubernetesClient kubernetesClient;

    @Inject
    CrdMapper crdMapper;
    private static final String GROUP = "onecx.tkit.org";
    private static ResourceDefinitionContext DATA_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("datas")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext DATABASE_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("databases")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext KEYCLOAKCLIENT_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("keycloakclients")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext MICROFRONTEND_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("microfrontends")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext MICROSERVICE_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("microservices")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext PERMISSION_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("permissions")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext PRODUCT_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("products")
            .withNamespaced(true)
            .build();
    private static ResourceDefinitionContext SLOT_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("slots")
            .withNamespaced(true)
            .build();

    List<ResourceDefinitionContext> contextList = List.of(DATA_CONTEXT, DATABASE_CONTEXT, KEYCLOAKCLIENT_CONTEXT,
            MICROFRONTEND_CONTEXT, MICROSERVICE_CONTEXT, PERMISSION_CONTEXT, PRODUCT_CONTEXT, SLOT_CONTEXT);

    @Override
    public Response editData(EditResourceRequestDTO requestDTO) {
        var namespace = kubernetesClient.getNamespace();
        if (requestDTO.getCrdData() != null) {
            var resourceDto = requestDTO.getCrdData();
            var editedResource = crdMapper.mapDataDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(DATA_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdDatabase() != null) {
            var resourceDto = requestDTO.getCrdDatabase();
            var editedResource = crdMapper.mapDatabaseDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(DATABASE_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdKeycloakClient() != null) {
            var resourceDto = requestDTO.getCrdKeycloakClient();
            var editedResource = crdMapper.mapKeycloakClientDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(KEYCLOAKCLIENT_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdMicrofrontend() != null) {
            var resourceDto = requestDTO.getCrdMicrofrontend();
            var editedResource = crdMapper.mapMicrofrontendDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(MICROFRONTEND_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdMicroservice() != null) {
            var resourceDto = requestDTO.getCrdMicroservice();
            var editedResource = crdMapper.mapMicroserviceDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(MICROSERVICE_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdPermission() != null) {
            var resourceDto = requestDTO.getCrdPermission();
            var editedResource = crdMapper.mapPermissionDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(PERMISSION_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdProduct() != null) {
            var resourceDto = requestDTO.getCrdProduct();
            var editedResource = crdMapper.mapProductDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(PRODUCT_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        if (requestDTO.getCrdSlot() != null) {
            var resourceDto = requestDTO.getCrdSlot();
            var editedResource = crdMapper.mapSlotDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(SLOT_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(404).build();
    }

    @Override
    public Response getAllCustomResources() {
        List<GenericKubernetesResource> genericKubernetesResourceList = new ArrayList<>();
        var namespace = kubernetesClient.getNamespace();
        contextList.forEach(customResourceDefinitionContext -> {
            var items = kubernetesClient.genericKubernetesResources(customResourceDefinitionContext)
                    .inNamespace(namespace).list()
                    .getItems();
            if (!items.isEmpty()) {
                genericKubernetesResourceList.addAll(items);
            }
        });
        CrdResponseDTO responseDTO = crdMapper.mapToResponse(genericKubernetesResourceList);
        return Response.status(Response.Status.OK).entity(responseDTO).build();
    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTO> constraint(ConstraintViolationException ex) {
        return exceptionMapper.constraint(ex);
    }

    @ServerExceptionMapper
    public RestResponse<ProblemDetailResponseDTO> kubernetesException(KubernetesClientException ex) {
        return exceptionMapper.kubernetesClientException(ex);
    }

}
