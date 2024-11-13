package org.tkit.onecx.data.orchestrator.bff.rs.mappers;

import java.util.List;
import java.util.Map;

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

    public GenericKubernetesResource mapDataDtoToGeneric(CustomResourceDataDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapDatabaseDtoToGeneric(CustomResourceDatabaseDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapKeycloakClientDtoToGeneric(CustomResourceKeycloakClientDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapPermissionDtoToGeneric(CustomResourcePermissionDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapProductDtoToGeneric(CustomResourceProductDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapSlotDtoToGeneric(CustomResourceSlotDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapMicrofrontendDtoToGeneric(CustomResourceMicrofrontendDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericKubernetesResource mapMicroserviceDtoToGeneric(CustomResourceMicroserviceDTO data) {
        GenericKubernetesResource genericKubernetesResource;
        genericKubernetesResource = mapper.convertValue(data, GenericKubernetesResource.class);
        return genericKubernetesResource;
    }

    public GenericCrdDTO mapToGenericCrdDTO(GenericKubernetesResource genericKubernetesResource) {
        final String STATUS = "status";
        GenericCrdDTO genericCrdDTO = new GenericCrdDTO();
        genericCrdDTO.setKind(genericKubernetesResource.getKind());
        genericCrdDTO.setName(genericKubernetesResource.getMetadata().getName());
        genericCrdDTO.setCreationTimestamp(genericKubernetesResource.getMetadata().getCreationTimestamp());
        genericCrdDTO.setResourceVersion(genericKubernetesResource.getMetadata().getResourceVersion());
        genericCrdDTO.setVersion(genericKubernetesResource.getMetadata().getLabels().get("version"));
        Map<String, String> status = (Map<String, String>) genericKubernetesResource.getAdditionalProperties().get(STATUS);
        if (status != null) {
            genericCrdDTO.setMessage(status.get("message"));
            genericCrdDTO.setStatus(GenericCrdDTO.StatusEnum.valueOf(status.get(STATUS)));
            Map<String, Integer> statusCode = (Map<String, Integer>) genericKubernetesResource.getAdditionalProperties()
                    .get(STATUS);
            genericCrdDTO.setResponseCode(statusCode.get("responseCode"));
        }
        return genericCrdDTO;
    }

    public abstract List<GenericCrdDTO> mapToGenericResourceDTOs(List<GenericKubernetesResource> genericKubernetesResourceList);

    public GetCRDResponseDTO mapToGetResponseObject(GenericKubernetesResource item) {
        GetCRDResponseDTO responseDTO = new GetCRDResponseDTO();
        Object object;
        object = mapper.convertValue(item, Object.class);
        responseDTO.setCrd(object);
        return responseDTO;
    }
}
