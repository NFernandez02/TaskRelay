package com.taskrelay.job.application;

import java.time.Clock;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskrelay.endpoint.application.EndpointService;
import com.taskrelay.job.domain.Job;
import com.taskrelay.job.infrastructure.JobRepository;

import tools.jackson.databind.JsonNode;

@Service 
public class JobService {

    private final JobRepository jobRepository;
    private final Clock clock;
    private final EndpointService endpointService;

    public JobService(JobRepository jobRepository, Clock clock, EndpointService endpointService) {
        this.jobRepository = jobRepository;
        this.clock = clock;
        this.endpointService = endpointService;
    }

    @Transactional
    public Job submit(UUID endpointId, JsonNode payload) {
        if(payload == null || !payload.isObject()) {
            throw new IllegalArgumentException("Payload must be a JSON object.");
        }
        endpointService.getById(endpointId);

        Job job = Job.submit(
                UUID.randomUUID(),
                endpointId,
                payload.toString(),
                Instant.now(clock)
        );

        return jobRepository.save(job);
    }

    @Transactional 
    public Job getById(UUID id){
        return jobRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Job not found " + id));
    }
}