
package com.water.distribution.service;

import com.water.distribution.entity.SupplyZone;
import com.water.distribution.repository.SupplyZoneRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplyZoneService {

    private final SupplyZoneRepository supplyZoneRepository;

    public SupplyZoneService(SupplyZoneRepository supplyZoneRepository) {
        this.supplyZoneRepository = supplyZoneRepository;
    }

    public SupplyZone addZone(SupplyZone zone) {
        return supplyZoneRepository.save(zone);
    }

    public List<SupplyZone> getAllZones() {
        return supplyZoneRepository.findAll();
    }

    public Optional<SupplyZone> getZoneById(Long id) {
        return supplyZoneRepository.findById(id);
    }

    public SupplyZone updateZone(Long id, SupplyZone updatedZone) {
        SupplyZone zone = supplyZoneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Zone not found"));

        zone.setName(updatedZone.getName());
        zone.setLocation(updatedZone.getLocation());
        zone.setPopulation(updatedZone.getPopulation());
        zone.setStatus(updatedZone.getStatus());

        return supplyZoneRepository.save(zone);
    }

    public void deleteZone(Long id) {
        if (!supplyZoneRepository.existsById(id)) {
            throw new RuntimeException("Zone not found");
        }

        supplyZoneRepository.deleteById(id);
    }
}
