package com.taskrelay.job.api;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskrelay.job.application.JobService;
import com.taskrelay.job.domain.Job;

import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController 
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;
    private final ObjectMapper objectMapper;

    public JobController(JobService jobService, ObjectMapper objectMapper){
        this.jobService = jobService;
        this.objectMapper = objectMapper;
    }

    @PostMapping 
    public ResponseEntity<JobResponse> submit(@Valid @RequestBody SubmitJobRequest request){
        Job job = jobService.submit(request.endpointId(), request.payload());

        return ResponseEntity.accepted().location(URI.create("/api/v1/jobs/" + job.getId())).body(JobResponse.from(job, objectMapper));
    }

    @GetMapping("/{id}")
    public JobResponse getById(@PathVariable UUID id){
        return JobResponse.from(jobService.getById(id), objectMapper);
    }
}
