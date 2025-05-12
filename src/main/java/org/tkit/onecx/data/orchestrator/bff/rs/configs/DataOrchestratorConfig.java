package org.tkit.onecx.data.orchestrator.bff.rs.configs;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigDocFilename;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

/**
 * Data orchestrator bff configuration
 */
@ConfigDocFilename("onecx-data-orchestrator-bff.adoc")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "onecx.data.orchestrator")
public interface DataOrchestratorConfig {

    /**
     * Configurations for each custom resource type
     */
    @WithName("types")
    Map<String, TypeConfig> types();

    /**
     * Configuration for a specific custom resource type
     */
    interface TypeConfig {
        /**
         * Enable or disable a specific resource
         */
        @WithDefault("true")
        @WithName("enabled")
        boolean enabled();

        /**
         * Kind value from custom resource definition
         */
        @WithName("value")
        String value();
    }
}
