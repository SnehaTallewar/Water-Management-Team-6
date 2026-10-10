
package com.water.distribution.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "distribution_schedules")
public class DistributionSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private SupplyZone supplyZone;

    @Column(nullable = false)
    private LocalDate distributionDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String status;
    private Integer availableLitres;
    private Integer allocatedLitres;

    public DistributionSchedule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SupplyZone getSupplyZone() {
        return supplyZone;
    }

    public void setSupplyZone(SupplyZone supplyZone) {
        this.supplyZone = supplyZone;
    }

    public LocalDate getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(LocalDate distributionDate) {
        this.distributionDate = distributionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getAvailableLitres() {
    return availableLitres;
}

public void setAvailableLitres(Integer availableLitres) {
    this.availableLitres = availableLitres;
}

public Integer getAllocatedLitres() {
    return allocatedLitres;
}

public void setAllocatedLitres(Integer allocatedLitres) {
    this.allocatedLitres = allocatedLitres;
   }
}
