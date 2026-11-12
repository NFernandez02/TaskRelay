package com.taskrelay.job.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskrelay.job.domain.Job;

public interface JobRepository extends JpaRepository<Job, UUID> {
}