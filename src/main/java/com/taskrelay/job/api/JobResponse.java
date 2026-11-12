package com.taskrelay.job.api;

import java.time.Instant;
import java.util.UUID;

import com.taskrelay.job.domain.Job;
import com.taskrelay.job.domain.JobStatus;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public record JobResponse(UUID id, UUID endpointId, JsonNode payload, JobStatus status, Instant availableAt, int attemptCount, Instant createdAt) {
    public static JobResponse from(Job job, ObjectMapper objectMapper) {
        try {
            return new JobResponse(
                job.getId(),
                job.getEndpointId(),
                objectMapper.readTree(job.getPayload()),
                job.getStatus(),
                job.getAvailableAt(),
                job.getAttemptCount(),
                job.getCreatedAt()
            );
        } catch (JacksonException ex){
            throw new IllegalStateException("Stored job payload is not a valid JSON.", ex);
        }
    }    
}
