package org.tkit.onecx.data.orchestrator.bff.rs.services;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.tkit.onecx.data.orchestrator.bff.rs.configs.DataOrchestratorConfig;

import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.ContextKindDTO;
import gen.org.tkit.onecx.data.orchestrator.bff.rs.internal.model.GetContextKindsResponseDTO;

@ApplicationScoped
public class DataService {

    @Inject
    DataOrchestratorConfig config;

    public Set<ContextKindDTO> getActiveTypes() {
        GetContextKindsResponseDTO responseDTO = new GetContextKindsResponseDTO();
        return config.types().values().stream().filter(DataOrchestratorConfig.TypeConfig::enabled)
                .map(typeConfig -> ContextKindDTO.fromString(typeConfig.value())).collect(Collectors.toSet());
    }

}
