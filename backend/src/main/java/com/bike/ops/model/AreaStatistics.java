package com.bike.ops.model;

import java.time.LocalDateTime;

public class AreaStatistics {
    private String area;
    private Integer totalBikes;
    private Integer availableBikes;
    private Integer lowBatteryBikes;
    private Integer inUseBikes;
    private Integer maintenanceBikes;
    private Integer parkingSpots;
    private Integer overCapacitySpots;
    private Integer pendingTasks;
    private LocalDateTime statisticsTime;

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Integer getTotalBikes() { return totalBikes; }
    public void setTotalBikes(Integer totalBikes) { this.totalBikes = totalBikes; }
    public Integer getAvailableBikes() { return availableBikes; }
    public void setAvailableBikes(Integer availableBikes) { this.availableBikes = availableBikes; }
    public Integer getLowBatteryBikes() { return lowBatteryBikes; }
    public void setLowBatteryBikes(Integer lowBatteryBikes) { this.lowBatteryBikes = lowBatteryBikes; }
    public Integer getInUseBikes() { return inUseBikes; }
    public void setInUseBikes(Integer inUseBikes) { this.inUseBikes = inUseBikes; }
    public Integer getMaintenanceBikes() { return maintenanceBikes; }
    public void setMaintenanceBikes(Integer maintenanceBikes) { this.maintenanceBikes = maintenanceBikes; }
    public Integer getParkingSpots() { return parkingSpots; }
    public void setParkingSpots(Integer parkingSpots) { this.parkingSpots = parkingSpots; }
    public Integer getOverCapacitySpots() { return overCapacitySpots; }
    public void setOverCapacitySpots(Integer overCapacitySpots) { this.overCapacitySpots = overCapacitySpots; }
    public Integer getPendingTasks() { return pendingTasks; }
    public void setPendingTasks(Integer pendingTasks) { this.pendingTasks = pendingTasks; }
    public LocalDateTime getStatisticsTime() { return statisticsTime; }
    public void setStatisticsTime(LocalDateTime statisticsTime) { this.statisticsTime = statisticsTime; }
}
