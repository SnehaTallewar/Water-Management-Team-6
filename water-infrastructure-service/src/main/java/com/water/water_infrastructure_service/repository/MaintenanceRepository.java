
package com.water.water_infrastructure_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.water.water_infrastructure_service.entity.Maintenance;
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
