
package com.water.water_infrastructure_service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
public class PipelineRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String startLocation;
    @NotBlank
    private String endLocation;
    @Positive
    private double length;
    @NotBlank
    private String status;
    public PipelineRequestDTO() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStartLocation() {
        return startLocation;
    }
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }
    public String getEndLocation() {
        return endLocation;
    }
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
