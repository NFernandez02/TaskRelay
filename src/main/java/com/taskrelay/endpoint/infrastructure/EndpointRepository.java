package com.taskrelay.endpoint.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskrelay.endpoint.domain.Endpoint;

public interface EndpointRepository extends JpaRepository<Endpoint, UUID> {
}
