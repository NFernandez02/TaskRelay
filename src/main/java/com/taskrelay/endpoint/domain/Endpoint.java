package com.taskrelay.endpoint.domain;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "endpoints")
public class Endpoint {

    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Endpoint() {
    }

    public Endpoint(
            UUID id,
            String name,
            String url,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = name;
        this.url = url;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Endpoint register(UUID id, String name, String url, Instant now){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Endpoint name can't be blank.");
        }

        if(name.length() > 100){
            throw new IllegalArgumentException("Endpoint name can't exceed 100 characters.");
        }

        if(url == null || url.isBlank()){
            throw new IllegalArgumentException("Endpoint URL can't be blank.");
        }
        
        URI uri;
        try{
            uri = URI.create(url);
        } catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Endpoint URL is not a valid URL");
        }

        String scheme = uri.getScheme();

        if(scheme == null || !(scheme.toLowerCase(Locale.ROOT).equals("http") || (scheme.toLowerCase(Locale.ROOT).equals("https")))
            || uri.getHost() == null || uri.getUserInfo() != null){
            throw new IllegalArgumentException("Endpoint URL must be an HTTP or HTTPS URL " + "with a hostname and no embedded credentials");
        }

        return new Endpoint(Objects.requireNonNull(id), name.trim(), uri.toASCIIString(), now);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}