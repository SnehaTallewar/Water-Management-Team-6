
package com.water.water_infrastructure_service.controller;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.water.water_infrastructure_service.repository.PumpRepository;
import com.water.water_infrastructure_service.repository.PipelineRepository;
import com.water.water_infrastructure_service.repository.MaintenanceRepository;
@RestController
@RequestMapping("/api/infrastructure")
public class InfrastructureController {
    private final PumpRepository pumpRepository;
    private final PipelineRepository pipelineRepository;
    private final MaintenanceRepository maintenanceRepository;
    public InfrastructureController(
            PumpRepository pumpRepository,
            PipelineRepository pipelineRepository,
            MaintenanceRepository maintenanceRepository) {
        this.pumpRepository = pumpRepository;
        this.pipelineRepository = pipelineRepository;
        this.maintenanceRepository = maintenanceRepository;
    }
    @GetMapping
    public Map<String, Object> getInfrastructure() {
        Map<String, Object> result = new HashMap<>();
        result.put("pumps", pumpRepository.findAll());
        result.put("pipelines", pipelineRepository.findAll());
        result.put("maintenance", maintenanceRepository.findAll());
        return result;
    }
}
