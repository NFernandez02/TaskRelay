package com.taskrelay.job.api;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record SubmitJobRequest(
    @NotNull UUID endpointId,
    @NotNull JsonNode payload
) {
    
}
