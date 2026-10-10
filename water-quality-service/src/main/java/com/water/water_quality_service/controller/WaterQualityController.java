
package com.water.water_quality_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.water.water_quality_service.dto.WaterQualityDTO;
import com.water.water_quality_service.service.WaterQualityService;

@RestController
@RequestMapping("/api/quality")
public class WaterQualityController {

    private final WaterQualityService service;

    public WaterQualityController(WaterQualityService service) {
        this.service = service;
    }

    // HEALTH CHECK
    @GetMapping("/health")
    public String health() {
        return "Water Quality Service is running!";
    }

    // CREATE
    @PostMapping
    public WaterQualityDTO saveQuality(
            @RequestBody WaterQualityDTO quality) {
        return service.saveQuality(quality);
    }

    // READ ALL
    @GetMapping
    public List<WaterQualityDTO> getAllQuality() {
        return service.getAllQuality();
    }

    // READ ONE
    @GetMapping("/{id}")
    public WaterQualityDTO getQualityById(
            @PathVariable Long id) {
        return service.getQualityById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public WaterQualityDTO updateQuality(
            @PathVariable Long id,
            @RequestBody WaterQualityDTO quality) {
        return service.updateQuality(id, quality);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuality(
            @PathVariable Long id) {
        service.deleteQuality(id);
        return ResponseEntity.noContent().build();
    }
}
