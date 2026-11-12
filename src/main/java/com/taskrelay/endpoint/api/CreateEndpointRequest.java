package com.taskrelay.endpoint.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateEndpointRequest (
    @NotBlank
    @Size(max = 100)
    String name,

    @NotBlank
    @Size(max = 2048)
    String url
) {
    
}
