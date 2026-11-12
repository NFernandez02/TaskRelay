package com.taskrelay.job.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    private UUID id;

    @Column(name = "endpoint_id", nullable = false)
    private UUID endpointId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobStatus status;

    @Column(name = "available_at", nullable = false)
    private Instant availableAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Job() {
    }

    private Job(
            UUID id,
            UUID endpointId,
            String payload,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.endpointId = Objects.requireNonNull(endpointId);
        this.payload = Objects.requireNonNull(payload);
        this.createdAt = Objects.requireNonNull(createdAt);

        this.status = JobStatus.QUEUED;
        this.availableAt = createdAt;
        this.attemptCount = 0;
    }

    public static Job submit(
            UUID id,
            UUID endpointId,
            String payload,
            Instant now
    ) {
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("Payload cannot be blank");
        }

        return new Job(id, endpointId, payload, now);
    }

    public void markRunning() {
        requireStatus(JobStatus.QUEUED);

        status = JobStatus.RUNNING;
        attemptCount++;
    }

    public void markSucceeded() {
        requireStatus(JobStatus.RUNNING);

        status = JobStatus.SUCCEEDED;
    }

    public void scheduleRetry(Instant nextAttemptAt) {
        requireStatus(JobStatus.RUNNING);

        availableAt = Objects.requireNonNull(nextAttemptAt);
        status = JobStatus.QUEUED;
    }

    public void markFailed() {
        requireStatus(JobStatus.RUNNING);

        status = JobStatus.FAILED;
    }

    public void cancel() {
        requireStatus(JobStatus.QUEUED);

        status = JobStatus.CANCELLED;
    }

    private void requireStatus(JobStatus expected) {
        if (status != expected) {
            throw new IllegalStateException(
                    "Invalid job state transition from " + status
            );
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getEndpointId() {
        return endpointId;
    }

    public String getPayload() {
        return payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Instant getAvailableAt() {
        return availableAt;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}