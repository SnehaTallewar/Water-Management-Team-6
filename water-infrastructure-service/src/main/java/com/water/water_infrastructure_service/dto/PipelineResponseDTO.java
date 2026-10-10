
package com.water.water_infrastructure_service.dto;
public class PipelineResponseDTO {
    private Long id;
    private String name;
    private String startLocation;
    private String endLocation;
    private double length;
    private String status;
    public PipelineResponseDTO() {
    }
    public PipelineResponseDTO(Long id, String name, String startLocation,String endLocation, double length, String status) {
        this.id = id;
        this.name = name;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.length = length;
        this.status = status;
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
