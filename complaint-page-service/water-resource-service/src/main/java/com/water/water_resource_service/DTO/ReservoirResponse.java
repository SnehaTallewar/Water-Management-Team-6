package com.water.water_resource_service.DTO;

public class ReservoirResponse {

    private Long id;
    private String name;
    private String location;
    private Double capacity;
    private Double currentWaterLevel;
    private Double inflow;
    private Double outflow;
    private Double waterPercentage;
    private boolean lowWater;

    public ReservoirResponse() {
    }

    public ReservoirResponse(
            Long id,
            String name,
            String location,
            Double capacity,
            Double currentWaterLevel,
            Double inflow,
            Double outflow,
            Double waterPercentage,
            boolean lowWater) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.currentWaterLevel = currentWaterLevel;
        this.inflow = inflow;
        this.outflow = outflow;
        this.waterPercentage = waterPercentage;
        this.lowWater = lowWater;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Double getCapacity() {
        return capacity;
    }

    public Double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public Double getInflow() {
        return inflow;
    }

    public Double getOutflow() {
        return outflow;
    }

    public Double getWaterPercentage() {
        return waterPercentage;
    }

    public boolean isLowWater() {
        return lowWater;
    }
}