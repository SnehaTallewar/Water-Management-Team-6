package com.water.water_resource_service.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.water.water_resource_service.entity.Reservoir;
public interface ReservoirRepository extends JpaRepository<Reservoir,Long>{
	List<Reservoir> findByCurrentWaterLevelLessThanEqual(Double waterLevel);
}