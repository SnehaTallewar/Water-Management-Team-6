
package com.water.water_quality_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "quality_tests")
public class WaterQualityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "test_date")
    private LocalDate testDate;

    @Column(name = "inspection_date")
    private LocalDate inspectionDate;

    @Column(name = "ph")
    private Double ph;

    @Column(name = "hardness")
    private Double hardness;

    @Column(name = "solids")
    private Double solids;

    @Column(name = "chloramines")
    private Double chloramines;

    @Column(name = "sulfate")
    private Double sulfate;

    @Column(name = "conductivity")
    private Double conductivity;

    @Column(name = "organic_carbon")
    private Double organicCarbon;

    @Column(name = "trihalomethanes")
    private Double trihalomethanes;

    @Column(name = "turbidity")
    private Double turbidity;

    @Column(name = "compliance_status")
    private String complianceStatus = "PENDING";

 

    public WaterQualityEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public Double getPh() {
        return ph;
    }

    public void setPh(Double ph) {
        this.ph = ph;
    }

    public Double getHardness() {
        return hardness;
    }

    public void setHardness(Double hardness) {
        this.hardness = hardness;
    }

    public Double getSolids() {
        return solids;
    }

    public void setSolids(Double solids) {
        this.solids = solids;
    }

    public Double getChloramines() {
        return chloramines;
    }

    public void setChloramines(Double chloramines) {
        this.chloramines = chloramines;
    }

    public Double getSulfate() {
        return sulfate;
    }

    public void setSulfate(Double sulfate) {
        this.sulfate = sulfate;
    }

    public Double getConductivity() {
        return conductivity;
    }

    public void setConductivity(Double conductivity) {
        this.conductivity = conductivity;
    }

    public Double getOrganicCarbon() {
        return organicCarbon;
    }

    public void setOrganicCarbon(Double organicCarbon) {
        this.organicCarbon = organicCarbon;
    }

    public Double getTrihalomethanes() {
        return trihalomethanes;
    }

    public void setTrihalomethanes(Double trihalomethanes) {
        this.trihalomethanes = trihalomethanes;
    }

    public Double getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(Double turbidity) {
        this.turbidity = turbidity;
    }

    public String getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    
}
