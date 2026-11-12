package com.taskrelay.endpoint.application;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import com.taskrelay.endpoint.domain.Endpoint;
import com.taskrelay.endpoint.infrastructure.EndpointRepository;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class EndpointService {
    private final EndpointRepository repository;
    private final Clock clock;

    public EndpointService(EndpointRepository repository, Clock clock){
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional 
    public Endpoint register(String name, String url){
        Endpoint endpoint = Endpoint.register(UUID.randomUUID(), name, url, Instant.now(clock));
        return repository.save(endpoint);
    }

    @Transactional(readOnly = true)
    public Endpoint getById(UUID id){
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Endpoint not found: " + id));
    }
}
