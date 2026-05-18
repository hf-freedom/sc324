package com.bike.ops.model;

import java.time.LocalDateTime;

public class AbnormalRecord {
    private String id;
    private String bikeId;
    private String parkingSpotId;
    private String description;
    private String area;
    private LocalDateTime createTime;
    private Boolean resolved;
    private LocalDateTime resolveTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public String getParkingSpotId() { return parkingSpotId; }
    public void setParkingSpotId(String parkingSpotId) { this.parkingSpotId = parkingSpotId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
    public LocalDateTime getResolveTime() { return resolveTime; }
    public void setResolveTime(LocalDateTime resolveTime) { this.resolveTime = resolveTime; }
}
