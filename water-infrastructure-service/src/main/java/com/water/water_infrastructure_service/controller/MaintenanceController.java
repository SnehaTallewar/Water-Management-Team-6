
package com.water.water_infrastructure_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.water.water_infrastructure_service.dto.MaintenanceRequestDTO;
import com.water.water_infrastructure_service.dto.MaintenanceResponseDTO;
import com.water.water_infrastructure_service.entity.Maintenance;
import com.water.water_infrastructure_service.repository.MaintenanceRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceController(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    private MaintenanceResponseDTO convertToDTO(Maintenance maintenance) {
        return new MaintenanceResponseDTO(
                maintenance.getId(),
                maintenance.getPumpId(),
                maintenance.getDescription(),
                maintenance.getMaintenanceDate(),
                maintenance.getStatus()
        );
    }

    private void updateMaintenance(Maintenance maintenance, MaintenanceRequestDTO dto) {
        maintenance.setPumpId(dto.getPumpId());
        maintenance.setDescription(dto.getDescription());
        maintenance.setMaintenanceDate(dto.getMaintenanceDate());
        maintenance.setStatus(dto.getStatus());
    }

    @PostMapping
    public MaintenanceResponseDTO addMaintenance(
            @Valid @RequestBody MaintenanceRequestDTO dto) {
        Maintenance maintenance = new Maintenance();
        updateMaintenance(maintenance, dto);
        return convertToDTO(maintenanceRepository.save(maintenance));
    }

    @GetMapping
    public List<MaintenanceResponseDTO> getAllMaintenance() {
        return maintenanceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> getMaintenance(@PathVariable Long id) {
        return maintenanceRepository.findById(id)
                .map(maintenance -> ResponseEntity.ok(convertToDTO(maintenance)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenanceById(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequestDTO dto) {
        return maintenanceRepository.findById(id).map(maintenance -> {
            updateMaintenance(maintenance, dto);
            return ResponseEntity.ok(
                    convertToDTO(maintenanceRepository.save(maintenance)));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        if (!maintenanceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        maintenanceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
