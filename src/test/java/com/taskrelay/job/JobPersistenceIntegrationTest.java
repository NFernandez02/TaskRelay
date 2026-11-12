package com.taskrelay.job;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.taskrelay.TestcontainersConfiguration;
import com.taskrelay.endpoint.application.EndpointService;
import com.taskrelay.endpoint.domain.Endpoint;
import com.taskrelay.job.application.JobService;
import com.taskrelay.job.domain.Job;
import com.taskrelay.job.domain.JobStatus;
import com.taskrelay.job.infrastructure.JobRepository;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest 
@Import (TestcontainersConfiguration.class)
public class JobPersistenceIntegrationTest {
    @Autowired 
    private EndpointService endpointService;

    @Autowired 
    private JobService jobService;

    @Autowired 
    private JobRepository jobRepository;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test 
    void submittedJobIsPesistentAndCanBeRetrieved() throws Exception {
        Endpoint endpoint = endpointService.register("invoice-service", "https://example.com/webhooks/invoice");

        JsonNode payload = jsonMapper.readTree("{\"invoiceId\":\"inv_123\"}");

        Job submittedJob = jobService.submit(endpoint.getId(), payload);

        Job persistedJob = jobRepository.findById(submittedJob.getId()).orElseThrow();

        assertThat(persistedJob.getId()).isEqualTo(submittedJob.getId());
        assertThat(persistedJob.getEndpointId()).isEqualTo(endpoint.getId());
        assertThat(persistedJob.getStatus()).isEqualTo(JobStatus.QUEUED);
        assertThat(persistedJob.getPayload()).isEqualTo("{\"invoiceId\":\"inv_123\"}");
        assertThat(persistedJob.getAttemptCount()).isZero();
        assertThat(persistedJob.getCreatedAt()).isNotNull();
    }
}
