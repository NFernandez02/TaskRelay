package com.taskrelay.job.application;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.taskrelay.endpoint.application.EndpointService;
import com.taskrelay.endpoint.domain.Endpoint;
import com.taskrelay.job.domain.Job;
import com.taskrelay.job.domain.JobStatus;
import com.taskrelay.job.infrastructure.JobRepository;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.JsonNode;

class JobServiceTest {

    private static final Instant NOW = Instant.parse("2026-10-07T12:00:00Z");

    private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void submitsAndPersistsQueuedJob() throws Exception{

        JobRepository repository = mock(JobRepository.class);
        EndpointService endpointService = mock(EndpointService.class);

        UUID endpointId = UUID.randomUUID();

        Endpoint endpoint = Endpoint.register(endpointId, "invoice-service", "https://example.com/webhooks/invoice", NOW);

        when(endpointService.getById(endpointId)).thenReturn(endpoint);

        when(repository.save(any(Job.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        JobService jobService = new JobService(repository, clock, endpointService);

        JsonNode payload = jsonMapper.readTree("{\"invoiceId\":\"123\"}");

        Job job = jobService.submit(endpointId, payload);

        assertThat(job.getEndpointId())
                .isEqualTo(endpointId);

        assertThat(job.getStatus())
                .isEqualTo(JobStatus.QUEUED);

        assertThat(job.getCreatedAt())
                .isEqualTo(NOW);
        
        assertThat(job.getPayload()).isEqualTo("{\"invoiceId\":\"123\"}");

        verify(endpointService).getById(endpointId);
        verify(repository).save(job);
    }
}