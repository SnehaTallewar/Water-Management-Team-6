
package com.water.distribution.controller;

import com.water.distribution.entity.SupplyZone;
import com.water.distribution.service.SupplyZoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class SupplyZoneController {

    private final SupplyZoneService supplyZoneService;

    public SupplyZoneController(SupplyZoneService supplyZoneService) {
        this.supplyZoneService = supplyZoneService;
    }

    @PostMapping
    public SupplyZone addZone(@RequestBody SupplyZone zone) {
        return supplyZoneService.addZone(zone);
    }

    @GetMapping
    public List<SupplyZone> getAllZones() {
        return supplyZoneService.getAllZones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyZone> getZoneById(@PathVariable Long id) {
        return supplyZoneService.getZoneById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyZone> updateZone(
            @PathVariable Long id,
            @RequestBody SupplyZone zone) {
        try {
            return ResponseEntity.ok(
                supplyZoneService.updateZone(id, zone));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        try {
            supplyZoneService.deleteZone(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
