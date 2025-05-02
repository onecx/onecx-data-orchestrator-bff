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
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.*;
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
    @SuppressWarnings("java:S3008")
    private static ResourceDefinitionContext PARAMETER_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withPlural("parameters")
            .withNamespaced(true)
            .build();

    private Map<String, ResourceDefinitionContext> contextMap = createContextMap();

    private static Map<String, ResourceDefinitionContext> createContextMap() {
        Map<String, ResourceDefinitionContext> map = new HashMap<>();
        map.put("Data", DATA_CONTEXT);
        map.put("Database", DATABASE_CONTEXT);
        map.put("KeycloakClient", KEYCLOAKCLIENT_CONTEXT);
        map.put("Microfrontend", MICROFRONTEND_CONTEXT);
        map.put("Microservice", MICROSERVICE_CONTEXT);
        map.put("Permission", PERMISSION_CONTEXT);
        map.put("Product", PRODUCT_CONTEXT);
        map.put("Slot", SLOT_CONTEXT);
        map.put("Parameter", PARAMETER_CONTEXT);
        return map;
    }

    @Override
    public Response getCustomResourcesByCriteria(CrdSearchCriteriaDTO crdSearchCriteriaDTO) {
        List<GenericKubernetesResource> genericKubernetesResourceList = new ArrayList<>();
        var namespace = kubernetesClient.getNamespace();
        if (crdSearchCriteriaDTO.getName() != null && !crdSearchCriteriaDTO.getName().isEmpty()) {
            crdSearchCriteriaDTO.getType().forEach(contextKindDTO -> {
                var context = contextMap.get(contextKindDTO.toString());
                var item = kubernetesClient.genericKubernetesResources(context)
                        .inNamespace(namespace).withName(crdSearchCriteriaDTO.getName()).get();
                if (item != null) {
                    genericKubernetesResourceList.add(item);
                }

            });
        } else {
            crdSearchCriteriaDTO.getType().forEach(contextKindDTO -> {
                var context = contextMap.get(contextKindDTO.toString());
                var items = kubernetesClient.genericKubernetesResources(context)
                        .inNamespace(namespace).list().getItems();
                genericKubernetesResourceList.addAll(items);

            });
        }
        CrdResponseDTO responseDTO = new CrdResponseDTO();
        responseDTO.setCustomResources(crdMapper.mapToGenericResourceDTOs(genericKubernetesResourceList));
        return Response.status(Response.Status.OK).entity(responseDTO).build();
    }

    @Override
    public Response touchCrdByNameAndType(String type, String name) {
        var namespace = kubernetesClient.getNamespace();
        var item = kubernetesClient.genericKubernetesResources(contextMap.get(type)).inNamespace(namespace).withName(name)
                .get();
        kubernetesClient.genericKubernetesResources(contextMap.get(type)).inNamespace(namespace).resource(item)
                .update();
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response editCrd(EditResourceRequestDTO requestDTO) {

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
        if (requestDTO.getCrdParameter() != null) {
            var resourceDto = requestDTO.getCrdParameter();
            var editedResource = crdMapper.mapParameterDtoToGeneric(resourceDto);
            kubernetesClient.genericKubernetesResources(PARAMETER_CONTEXT).inNamespace(namespace).resource(editedResource)
                    .update();
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(404).build();
    }

    @Override
    public Response getCrdByTypeAndName(ContextKindDTO type, String name) {
        var namespace = kubernetesClient.getNamespace();
        var context = contextMap.get(type.toString());
        var item = kubernetesClient.genericKubernetesResources(context)
                .inNamespace(namespace).withName(name).get();
        return Response.status(200).entity(crdMapper.mapToGetResponseObject(item)).build();
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
