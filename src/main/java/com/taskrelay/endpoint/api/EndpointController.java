package com.taskrelay.endpoint.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskrelay.endpoint.application.EndpointService;
import com.taskrelay.endpoint.domain.Endpoint;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController 
@RequestMapping ("/api/v1/endpoints")
public class EndpointController {
    private final EndpointService service;

    public EndpointController(EndpointService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EndpointResponse> register(@Valid @RequestBody CreateEndpointRequest request) {
        Endpoint endpoint = service.register(request.name(), request.url());

        EndpointResponse response = EndpointResponse.from(endpoint);

        return ResponseEntity.created(URI.create("/api/v1/endpoints/" + endpoint.getId())).body(response);
    }
    
    @GetMapping("/{id}")
    public EndpointResponse getById(@PathVariable UUID id) {
        return EndpointResponse.from(service.getById(id));
    }
    
}
