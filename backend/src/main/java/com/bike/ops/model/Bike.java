package com.bike.ops.model;

import com.bike.ops.model.enums.BikeStatus;
import java.time.LocalDateTime;

public class Bike {
    private String id;
    private String bikeNo;
    private Double longitude;
    private Double latitude;
    private Integer batteryLevel;
    private BikeStatus status;
    private String parkingSpotId;
    private String area;
    private LocalDateTime lastRideEndTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBikeNo() { return bikeNo; }
    public void setBikeNo(String bikeNo) { this.bikeNo = bikeNo; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }
    public BikeStatus getStatus() { return status; }
    public void setStatus(BikeStatus status) { this.status = status; }
    public String getParkingSpotId() { return parkingSpotId; }
    public void setParkingSpotId(String parkingSpotId) { this.parkingSpotId = parkingSpotId; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public LocalDateTime getLastRideEndTime() { return lastRideEndTime; }
    public void setLastRideEndTime(LocalDateTime lastRideEndTime) { this.lastRideEndTime = lastRideEndTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
