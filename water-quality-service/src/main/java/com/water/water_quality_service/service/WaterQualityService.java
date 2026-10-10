
package com.water.water_quality_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.water.water_quality_service.dto.WaterQualityDTO;
import com.water.water_quality_service.entity.WaterQualityEntity;
import com.water.water_quality_service.repository.WaterQualityRepository;

@Service
public class WaterQualityService {

    private final WaterQualityRepository repository;

    public WaterQualityService(WaterQualityRepository repository) {
        this.repository = repository;
    }

    // CONVERT DTO TO ENTITY
    private WaterQualityEntity toEntity(WaterQualityDTO dto) {
        WaterQualityEntity entity = new WaterQualityEntity();

        entity.setResourceId(dto.getResourceId());
        entity.setTestDate(dto.getTestDate());
        entity.setInspectionDate(dto.getInspectionDate());
        entity.setPh(dto.getPh());
        entity.setHardness(dto.getHardness());
        entity.setSolids(dto.getSolids());
        entity.setChloramines(dto.getChloramines());
        entity.setSulfate(dto.getSulfate());
        entity.setConductivity(dto.getConductivity());
        entity.setOrganicCarbon(dto.getOrganicCarbon());
        entity.setTrihalomethanes(dto.getTrihalomethanes());
        entity.setTurbidity(dto.getTurbidity());

        entity.setComplianceStatus(
                dto.getComplianceStatus() == null
                        ? "PENDING"
                        : dto.getComplianceStatus()
        );

      

        return entity;
    }

    // CONVERT ENTITY TO DTO
    private WaterQualityDTO toDTO(WaterQualityEntity entity) {
        WaterQualityDTO dto = new WaterQualityDTO();

        dto.setResourceId(entity.getResourceId());
        dto.setTestDate(entity.getTestDate());
        dto.setInspectionDate(entity.getInspectionDate());
        dto.setPh(entity.getPh());
        dto.setHardness(entity.getHardness());
        dto.setSolids(entity.getSolids());
        dto.setChloramines(entity.getChloramines());
        dto.setSulfate(entity.getSulfate());
        dto.setConductivity(entity.getConductivity());
        dto.setOrganicCarbon(entity.getOrganicCarbon());
        dto.setTrihalomethanes(entity.getTrihalomethanes());
        dto.setTurbidity(entity.getTurbidity());
        dto.setComplianceStatus(entity.getComplianceStatus());
       

        return dto;
    }

    // CREATE
    public WaterQualityDTO saveQuality(WaterQualityDTO quality) {
        WaterQualityEntity entity = toEntity(quality);
        WaterQualityEntity saved = repository.save(entity);

        return toDTO(saved);
    }

    // READ ALL
    public List<WaterQualityDTO> getAllQuality() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // READ ONE
    public WaterQualityDTO getQualityById(Long id) {
        WaterQualityEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Water quality test not found: " + id
                ));

        return toDTO(entity);
    }

    // UPDATE
    public WaterQualityDTO updateQuality(
            Long id, WaterQualityDTO updatedQuality) {

        WaterQualityEntity existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Water quality test not found: " + id
                ));

        existing.setResourceId(updatedQuality.getResourceId());
        existing.setTestDate(updatedQuality.getTestDate());
        existing.setInspectionDate(updatedQuality.getInspectionDate());
        existing.setPh(updatedQuality.getPh());
        existing.setHardness(updatedQuality.getHardness());
        existing.setSolids(updatedQuality.getSolids());
        existing.setChloramines(updatedQuality.getChloramines());
        existing.setSulfate(updatedQuality.getSulfate());
        existing.setConductivity(updatedQuality.getConductivity());
        existing.setOrganicCarbon(updatedQuality.getOrganicCarbon());
        existing.setTrihalomethanes(updatedQuality.getTrihalomethanes());
        existing.setTurbidity(updatedQuality.getTurbidity());

        existing.setComplianceStatus(
                updatedQuality.getComplianceStatus() == null
                        ? "PENDING"
                        : updatedQuality.getComplianceStatus()
        );

        

        WaterQualityEntity saved = repository.save(existing);

        return toDTO(saved);
    }

    // DELETE
    public void deleteQuality(Long id) {
        WaterQualityEntity existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Water quality test not found: " + id
                ));

        repository.delete(existing);
    }
}
