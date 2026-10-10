
package com.water.water_infrastructure_service.dto;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
public class MaintenanceRequestDTO {
    @NotNull
    @Positive
    private Long pumpId;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate maintenanceDate;
    @NotBlank
    private String status;
    public MaintenanceRequestDTO() {
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

