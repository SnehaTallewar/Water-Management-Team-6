
package com.water.water_quality_service.dto;

import java.time.LocalDate;

public class WaterQualityDTO {

    private Long resourceId;
    private LocalDate testDate;
    private LocalDate inspectionDate;

    private Double ph;
    private Double hardness;
    private Double solids;
    private Double chloramines;
    private Double sulfate;
    private Double conductivity;
    private Double organicCarbon;
    private Double trihalomethanes;
    private Double turbidity;

    private String complianceStatus;
    private String inspectorNotes;

    public WaterQualityDTO() {
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

    public String getInspectorNotes() {
        return inspectorNotes;
    }

    public void setInspectorNotes(String inspectorNotes) {
        this.inspectorNotes = inspectorNotes;
    }
}
