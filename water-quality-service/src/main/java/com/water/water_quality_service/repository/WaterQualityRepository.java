
package com.water.water_quality_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.water.water_quality_service.entity.WaterQualityEntity;

public interface WaterQualityRepository
        extends JpaRepository<WaterQualityEntity, Long> {

}
