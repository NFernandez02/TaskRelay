package com.taskrelay.endpoint.api;

import java.time.Instant;
import java.util.UUID;

import com.taskrelay.endpoint.domain.Endpoint;

public record EndpointResponse(UUID id, String name, String url, Instant createdAt) {
    public static EndpointResponse from(Endpoint endpoint){
        return new EndpointResponse(
        endpoint.getId(),
        endpoint.getName(),
        endpoint.getUrl(),
        endpoint.getCreatedAt()
        );
    }

}
