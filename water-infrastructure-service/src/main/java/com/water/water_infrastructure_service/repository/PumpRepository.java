
package com.water.water_infrastructure_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.water.water_infrastructure_service.entity.Pump;
public interface PumpRepository extends JpaRepository<Pump,Long> {
}
