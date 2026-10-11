package com.water.water_web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/dashboard-api/reservoirs")
public class ReservoirActionController {

    private final RestClient client;

    public ReservoirActionController(
            RestClient.Builder builder,
            @Value("${resource.service.url:http://localhost:8081}") String resourceUrl) {
        this.client = builder.baseUrl(resourceUrl).build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object add(@RequestBody Map<String, Object> reservoir) {
        try {
            return client.post()
                    .uri("/api/reservoirs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(reservoir)
                    .retrieve()
                    .body(Object.class);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Could not add reservoir: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        try {
            client.delete().uri("/api/reservoirs/{id}", id).retrieve().toBodilessEntity();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Could not delete reservoir: " + e.getMessage());
        }
    }
}