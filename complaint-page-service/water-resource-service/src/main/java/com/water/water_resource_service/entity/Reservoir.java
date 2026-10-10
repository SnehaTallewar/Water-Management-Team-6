package com.water.water_resource_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="reservoirs")
public class Reservoir{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="reservoir name is required")
	private String name;
	
	@NotBlank(message="location is required")
	private String location;
	
	@NotNull
	@Positive(message="capacity must be greater than 0")
	private Double capacity;
	
	@NotNull
    @DecimalMin(value = "0.0", message = "water level cannot be negative")
    private Double currentWaterLevel;

    @NotNull
    @DecimalMin(value = "0.0", message = "inflow cannot be negative")
    private Double inflow;
    
	@NotNull
	@DecimalMin(value="0.0",message="outflow cannot be negative")
	private Double outflow;
	
	public Reservoir() {
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

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getCurrentWaterLevel() {
        return currentWaterLevel;
    }

    public void setCurrentWaterLevel(Double currentWaterLevel) {
        this.currentWaterLevel = currentWaterLevel;
    }

    public Double getInflow() {
        return inflow;
    }

    public void setInflow(Double inflow) {
        this.inflow = inflow;
    }

    public Double getOutflow() {
        return outflow;
    }

    public void setOutflow(Double outflow) {
        this.outflow = outflow;
    }
}