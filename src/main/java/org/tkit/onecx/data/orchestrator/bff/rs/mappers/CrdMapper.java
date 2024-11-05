package org.tkit.onecx.data.orchestrator.bff.rs.mappers;

import java.util.List;

import jakarta.inject.Inject;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.*;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

@Mapper(uses = { OffsetDateTimeMapper.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CrdMapper {
    @Inject
    ObjectMapper mapper;

    public CustomResourceDataDTO mapGenericToDataDTO(GenericKubernetesResource resource) {
        CustomResourceDataDTO dataDTO;
        dataDTO = mapper.convertValue(resource, CustomResourceDataDTO.class);
        return dataDTO;
    }

    public GenericKubernetesResource mapDataDtoToGeneric(CustomResourceDataDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourceDatabaseDTO mapGenericToDatabaseDTO(GenericKubernetesResource resource) {
        CustomResourceDatabaseDTO databaseDTO;
        databaseDTO = mapper.convertValue(resource, CustomResourceDatabaseDTO.class);
        return databaseDTO;
    }

    public GenericKubernetesResource mapDatabaseDtoToGeneric(CustomResourceDatabaseDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourceKeycloakClientDTO mapGenericToKeycloakClientDTO(GenericKubernetesResource resource) {
        CustomResourceKeycloakClientDTO keycloakClientDTO;
        keycloakClientDTO = mapper.convertValue(resource, CustomResourceKeycloakClientDTO.class);
        return keycloakClientDTO;
    }

    public GenericKubernetesResource mapKeycloakClientDtoToGeneric(CustomResourceKeycloakClientDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourcePermissionDTO mapGenericToPermissionDTO(GenericKubernetesResource resource) {
        CustomResourcePermissionDTO permissionDTO;
        permissionDTO = mapper.convertValue(resource, CustomResourcePermissionDTO.class);
        return permissionDTO;
    }

    public GenericKubernetesResource mapPermissionDtoToGeneric(CustomResourcePermissionDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourceProductDTO mapGenericToProductDTO(GenericKubernetesResource resource) {
        CustomResourceProductDTO productDTO;
        productDTO = mapper.convertValue(resource, CustomResourceProductDTO.class);
        return productDTO;
    }

    public GenericKubernetesResource mapProductDtoToGeneric(CustomResourceProductDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourceSlotDTO mapGenericToSlotDTO(GenericKubernetesResource resource) {
        CustomResourceSlotDTO slotDTO;
        slotDTO = mapper.convertValue(resource, CustomResourceSlotDTO.class);
        return slotDTO;
    }

    public GenericKubernetesResource mapSlotDtoToGeneric(CustomResourceSlotDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourceMicrofrontendDTO mapGenericToMicrofrontendDTO(GenericKubernetesResource resource) {
        CustomResourceMicrofrontendDTO microfrontendDTO;
        microfrontendDTO = mapper.convertValue(resource, CustomResourceMicrofrontendDTO.class);
        return microfrontendDTO;
    }

    public GenericKubernetesResource mapMicrofrontendDtoToGeneric(CustomResourceMicrofrontendDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CustomResourceMicroserviceDTO mapGenericToMicroserviceDTO(GenericKubernetesResource resource) {
        CustomResourceMicroserviceDTO microserviceDTO;
        microserviceDTO = mapper.convertValue(resource, CustomResourceMicroserviceDTO.class);
        return microserviceDTO;
    }

    public GenericKubernetesResource mapMicroserviceDtoToGeneric(CustomResourceMicroserviceDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public CrdResponseDTO mapToResponse(List<GenericKubernetesResource> genericKubernetesResourceList) {
        CrdResponseDTO responseDTO = new CrdResponseDTO();
        genericKubernetesResourceList.forEach(kubernetesResource -> {
            switch (kubernetesResource.getKind()) {
                case "Data" -> responseDTO.addCrdDatasItem(mapGenericToDataDTO(kubernetesResource));
                case "Database" -> responseDTO.addCrdDatabasesItem(mapGenericToDatabaseDTO(kubernetesResource));
                case "KeycloakClient" -> responseDTO
                        .addCrdKeycloakClientsItem(mapGenericToKeycloakClientDTO(kubernetesResource));
                case "Microfrontend" -> responseDTO.addCrdMicrofrontendsItem(mapGenericToMicrofrontendDTO(kubernetesResource));
                case "Microservice" -> responseDTO.addCrdMicroservicesItem(mapGenericToMicroserviceDTO(kubernetesResource));
                case "Permission" -> responseDTO.addCrdPermissionsItem(mapGenericToPermissionDTO(kubernetesResource));
                case "Product" -> responseDTO.addCrdProductsItem(mapGenericToProductDTO(kubernetesResource));
                case "Slot" -> responseDTO.addCrdSlotsItem(mapGenericToSlotDTO(kubernetesResource));

            }
        });
        return responseDTO;
    }

}
