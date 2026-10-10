package com.water.distribution.controller;

import com.water.distribution.entity.DistributionSchedule;
import com.water.distribution.service.DistributionScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class DistributionScheduleController {

    private final DistributionScheduleService scheduleService;

    public DistributionScheduleController(
            DistributionScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    @PostMapping
    public DistributionSchedule addSchedule(
            @RequestBody DistributionSchedule schedule) {
        return scheduleService.addSchedule(schedule);
    }
    @GetMapping
    public List<DistributionSchedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
    @GetMapping("/{id}")
    public ResponseEntity<DistributionSchedule> getScheduleById(
            @PathVariable Long id) {
        return scheduleService.getScheduleById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<DistributionSchedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody DistributionSchedule schedule) {
        try {
            return ResponseEntity.ok(
                scheduleService.updateSchedule(id, schedule));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{id}/allocate")
    public ResponseEntity<?> allocateWater(
            @PathVariable Long id,
            @RequestBody AllocationRequest body) {
        try {
            return ResponseEntity.ok(
                scheduleService.allocateWater(id, body.getLitres()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public static class AllocationRequest {
        private Integer litres;

        public Integer getLitres() {
            return litres;
        }

        public void setLitres(Integer litres) {
            this.litres = litres;
        }
    }
}
