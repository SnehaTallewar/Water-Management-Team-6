
package com.water.water_infrastructure_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.water.water_infrastructure_service.entity.Pipeline;
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
}

