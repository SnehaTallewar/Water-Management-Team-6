package com.water.distribution.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "water_tanks")
public class WaterTank{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	private String location;
	
	@Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal capacityLitres;
	@Column(nullable = false,precision = 12,scale = 2)
	private BigDecimal currentLevelLitres;
	
	private String status;
	
	public WaterTank() {
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
	
	public BigDecimal getCapacityLitres() {
        return capacityLitres;
    }

    public void setCapacityLitres(BigDecimal capacityLitres) {
        this.capacityLitres = capacityLitres;
    }

    public BigDecimal getCurrentLevelLitres() {
        return currentLevelLitres;
    }

    public void setCurrentLevelLitres(BigDecimal currentLevelLitres) {
        this.currentLevelLitres = currentLevelLitres;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}