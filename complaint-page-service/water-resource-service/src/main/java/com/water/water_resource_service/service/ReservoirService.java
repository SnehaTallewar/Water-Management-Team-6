package com.water.water_resource_service.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.water.water_resource_service.DTO.ReservoirResponse;
import com.water.water_resource_service.entity.Reservoir;
import com.water.water_resource_service.repository.ReservoirRepository;

@Service
public class ReservoirService {

    private final ReservoirRepository repository;

    private static final double LOW_WATER_THRESHOLD = 30.0;

    public ReservoirService(ReservoirRepository repository) {
        this.repository = repository;
    }

    
    public List<ReservoirResponse> getAllReservoirs() {
        return repository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    
    public ReservoirResponse getReservoirById(Long id) {
        Reservoir reservoir = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reservoir not found with id: " + id));

        return convertToResponse(reservoir);
    }

    
    public ReservoirResponse addReservoir(Reservoir reservoir) {
        validateWaterLevel(reservoir);

        reservoir.setId(null);

        Reservoir saved = repository.save(reservoir);

        return convertToResponse(saved);
    }

    
    public ReservoirResponse updateReservoir(
            Long id, Reservoir updatedReservoir) {

        Reservoir existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reservoir not found with id: " + id));

        validateWaterLevel(updatedReservoir);

        existing.setName(updatedReservoir.getName());
        existing.setLocation(updatedReservoir.getLocation());
        existing.setCapacity(updatedReservoir.getCapacity());
        existing.setCurrentWaterLevel(
                updatedReservoir.getCurrentWaterLevel());
        existing.setInflow(updatedReservoir.getInflow());
        existing.setOutflow(updatedReservoir.getOutflow());

        Reservoir saved = repository.save(existing);

        return convertToResponse(saved);
    }

   
    public void deleteReservoir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Reservoir not found with id: " + id);
        }

        repository.deleteById(id);
    }

    
    public List<ReservoirResponse> getLowWaterReservoirs() {
        return repository.findAll()
                .stream()
                .map(this::convertToResponse)
                .filter(ReservoirResponse::isLowWater)
                .toList();
    }

    
    private ReservoirResponse convertToResponse(Reservoir reservoir) {

        double percentage =
                (reservoir.getCurrentWaterLevel()
                        / reservoir.getCapacity()) * 100.0;

        boolean lowWater = percentage <= LOW_WATER_THRESHOLD;

        return new ReservoirResponse(
                reservoir.getId(),
                reservoir.getName(),
                reservoir.getLocation(),
                reservoir.getCapacity(),
                reservoir.getCurrentWaterLevel(),
                reservoir.getInflow(),
                reservoir.getOutflow(),
                Math.round(percentage * 100.0) / 100.0,
                lowWater
        );
    }

    
    private void validateWaterLevel(Reservoir reservoir) {

        if (reservoir.getCapacity() == null
                || reservoir.getCapacity() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Capacity must be greater than zero");
        }

        if (reservoir.getCurrentWaterLevel() == null
                || reservoir.getCurrentWaterLevel() < 0
                || reservoir.getCurrentWaterLevel()
                        > reservoir.getCapacity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Water level must be between zero and capacity");
        }
    }
}
