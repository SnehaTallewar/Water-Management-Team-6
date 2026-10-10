
package com.water.water_infrastructure_service.dto;
import java.time.LocalDate;
public class MaintenanceResponseDTO {
    private Long id;
    private Long pumpId;
    private String description;
    private LocalDate maintenanceDate;
    private String status;
    public MaintenanceResponseDTO() {
    }
    public MaintenanceResponseDTO(Long id, Long pumpId, String description,LocalDate maintenanceDate, String status) {
        this.id = id;
        this.pumpId = pumpId;
        this.description = description;
        this.maintenanceDate = maintenanceDate;
        this.status = status;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getPumpId() {
        return pumpId;
    }
    public void setPumpId(Long pumpId) {
        this.pumpId = pumpId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }
    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
