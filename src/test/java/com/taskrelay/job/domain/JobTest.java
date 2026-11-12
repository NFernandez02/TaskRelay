package com.taskrelay.job.domain;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class JobTest {

    @Test
    void submittedJobStartsQueued() {
        UUID endpointId = UUID.randomUUID();
        Instant now = Instant.now();

        Job job = Job.submit(
                UUID.randomUUID(),
                endpointId,
                "{\"invoiceId\":\"123\"}",
                now
        );

        assertThat(job.getStatus()).isEqualTo(JobStatus.QUEUED);
        assertThat(job.getAttemptCount()).isZero();
        assertThat(job.getAvailableAt()).isEqualTo(now);
    }

    @Test
    void runningJobCanSucceed() {
        Job job = createJob();

        job.markRunning();
        job.markSucceeded();

        assertThat(job.getStatus()).isEqualTo(JobStatus.SUCCEEDED);
        assertThat(job.getAttemptCount()).isEqualTo(1);
    }

    @Test
    void completedJobCannotRunAgain() {
        Job job = createJob();

        job.markRunning();
        job.markSucceeded();

        assertThatThrownBy(job::markRunning)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void runningJobCanBeScheduledForRetry() {
        Job job = createJob();

        job.markRunning();

        Instant retryAt = Instant.parse("2026-10-07T12:00:00Z");
        job.scheduleRetry(retryAt);

        assertThat(job.getStatus()).isEqualTo(JobStatus.QUEUED);
        assertThat(job.getAvailableAt()).isEqualTo(retryAt);
        assertThat(job.getAttemptCount()).isEqualTo(1);
    }

    private Job createJob() {
        return Job.submit(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "{\"invoiceId\":\"123\"}",
                Instant.now()
        );
    }
}