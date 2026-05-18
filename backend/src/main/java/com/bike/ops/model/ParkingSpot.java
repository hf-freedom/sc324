package com.bike.ops.model;

import java.time.LocalDateTime;
import java.util.List;

public class ParkingSpot {
    private String id;
    private String name;
    private Double longitude;
    private Double latitude;
    private Integer capacity;
    private String area;
    private List<String> bikeIds;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public int getCurrentBikeCount() {
        return bikeIds != null ? bikeIds.size() : 0;
    }

    public boolean isFull() {
        return getCurrentBikeCount() >= capacity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public List<String> getBikeIds() { return bikeIds; }
    public void setBikeIds(List<String> bikeIds) { this.bikeIds = bikeIds; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
