package com.water.water_resource_service.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.water.water_resource_service.DTO.ReservoirResponse;
import com.water.water_resource_service.entity.Reservoir;
import com.water.water_resource_service.service.ReservoirService;

@RestController
@RequestMapping("/api/reservoirs")
public class ReservoirController {

    private final ReservoirService service;

    public ReservoirController(ReservoirService service) {
        this.service = service;
    }

    
    @GetMapping
    public List<ReservoirResponse> getAllReservoirs() {
        return service.getAllReservoirs();
    }

   
    @GetMapping("/{id}")
    public ReservoirResponse getReservoirById(
            @PathVariable Long id) {
        return service.getReservoirById(id);
    }

    
    @GetMapping("/low-water")
    public List<ReservoirResponse> getLowWaterReservoirs() {
        return service.getLowWaterReservoirs();
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservoirResponse addReservoir(
            @Valid @RequestBody Reservoir reservoir) {
        return service.addReservoir(reservoir);
    }

    
    @PutMapping("/{id}")
    public ReservoirResponse updateReservoir(
            @PathVariable Long id,
            @Valid @RequestBody Reservoir reservoir) {
        return service.updateReservoir(id, reservoir);
    }

    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservoir(@PathVariable Long id) {
        service.deleteReservoir(id);
    }
}