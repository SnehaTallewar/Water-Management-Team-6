package com.water.distribution.controller;

import com.water.distribution.entity.WaterSupplyRequest;
import com.water.distribution.service.WaterSupplyRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/distributions")
public class WaterSupplyRequestController {
    private final WaterSupplyRequestService requestService;

    public WaterSupplyRequestController(
            WaterSupplyRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public WaterSupplyRequest addRequest(
            @RequestBody WaterSupplyRequest request) {
        return requestService.addRequest(request);
    }

    @GetMapping
    public List<WaterSupplyRequest> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterSupplyRequest> getRequestById(
            @PathVariable Long id) {
        return requestService.getRequestById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterSupplyRequest> updateRequest(
            @PathVariable Long id,
            @RequestBody WaterSupplyRequest request) {
        try {
            return ResponseEntity.ok(
                requestService.updateRequest(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable Long id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
