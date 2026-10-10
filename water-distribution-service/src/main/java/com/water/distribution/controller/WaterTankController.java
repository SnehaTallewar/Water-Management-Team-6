
package com.water.distribution.controller;

import com.water.distribution.entity.WaterTank;
import com.water.distribution.service.WaterTankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tanks")
public class WaterTankController {

    private final WaterTankService waterTankService;

    public WaterTankController(WaterTankService waterTankService) {
        this.waterTankService = waterTankService;
    }

    @PostMapping
    public WaterTank addTank(@RequestBody WaterTank tank) {
        return waterTankService.addTank(tank);
    }

    @GetMapping
    public List<WaterTank> getAllTanks() {
        return waterTankService.getAllTanks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterTank> getTankById(@PathVariable Long id) {
        return waterTankService.getTankById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterTank> updateTank(
            @PathVariable Long id,
            @RequestBody WaterTank tank) {
        try {
            return ResponseEntity.ok(
                    waterTankService.updateTank(id, tank));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTank(@PathVariable Long id) {
        try {
            waterTankService.deleteTank(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
