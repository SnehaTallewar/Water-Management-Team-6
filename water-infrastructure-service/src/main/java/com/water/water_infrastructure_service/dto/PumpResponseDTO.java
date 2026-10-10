
package com.water.water_infrastructure_service.dto;
public class PumpResponseDTO {
    private Long id;
    private String name;
    private String location;
    private String status;
    private Double capacity;
    public PumpResponseDTO() {
    }
    public PumpResponseDTO(Long id, String name, String location, String status, Double capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.status = status;
        this.capacity = capacity;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getCapacity() {
        return capacity;
    }
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
}

