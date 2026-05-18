package com.bike.ops.dto;

public class FaultReportRequest {
    private String bikeId;
    private String description;

    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
