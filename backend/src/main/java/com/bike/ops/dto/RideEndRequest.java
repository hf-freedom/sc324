package com.bike.ops.dto;

public class RideEndRequest {
    private String bikeId;
    private Double longitude;
    private Double latitude;
    private Integer batteryLevel;
    private String parkingSpotId;

    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }
    public String getParkingSpotId() { return parkingSpotId; }
    public void setParkingSpotId(String parkingSpotId) { this.parkingSpotId = parkingSpotId; }
}
