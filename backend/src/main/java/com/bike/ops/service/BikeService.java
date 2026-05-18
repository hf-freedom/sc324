package com.bike.ops.service;

import com.bike.ops.common.DataStore;
import com.bike.ops.dto.FaultReportRequest;
import com.bike.ops.model.Bike;
import com.bike.ops.model.MaintenanceTask;
import com.bike.ops.model.ParkingSpot;
import com.bike.ops.model.enums.BikeStatus;
import com.bike.ops.model.enums.TaskStatus;
import com.bike.ops.model.enums.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BikeService {

    @Autowired
    private DataStore dataStore;

    public List<Bike> getAllBikes() {
        return dataStore.bikes.values().stream().collect(Collectors.toList());
    }

    public List<Bike> getBikesByArea(String area) {
        return dataStore.bikes.values().stream()
                .filter(b -> area == null || area.equals(b.getArea()))
                .collect(Collectors.toList());
    }

    public List<Bike> getAvailableBikes() {
        return dataStore.bikes.values().stream()
                .filter(b -> b.getStatus() == BikeStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    public Bike startRide(String bikeId) {
        Bike bike = dataStore.bikes.get(bikeId);
        if (bike == null) {
            throw new RuntimeException("车辆不存在");
        }
        if (bike.getStatus() != BikeStatus.AVAILABLE) {
            throw new RuntimeException("车辆不可用，无法开始骑行");
        }

        if (bike.getParkingSpotId() != null) {
            ParkingSpot spot = dataStore.parkingSpots.get(bike.getParkingSpotId());
            if (spot != null) {
                spot.getBikeIds().remove(bike.getId());
                spot.setUpdateTime(LocalDateTime.now());
            }
        }

        bike.setStatus(BikeStatus.IN_USE);
        bike.setParkingSpotId(null);
        bike.setUpdateTime(LocalDateTime.now());
        return bike;
    }

    public List<Bike> getLowBatteryBikes() {
        return dataStore.bikes.values().stream()
                .filter(b -> b.getStatus() == BikeStatus.LOW_BATTERY)
                .collect(Collectors.toList());
    }

    public Bike reportFault(FaultReportRequest request) {
        Bike bike = dataStore.bikes.get(request.getBikeId());
        if (bike == null) {
            throw new RuntimeException("车辆不存在");
        }

        bike.setStatus(BikeStatus.MAINTENANCE);
        bike.setUpdateTime(LocalDateTime.now());

        MaintenanceTask task = new MaintenanceTask();
        task.setId(UUID.randomUUID().toString());
        task.setTaskType(TaskType.MAINTENANCE);
        task.setStatus(TaskStatus.PENDING);
        task.setBikeId(bike.getId());
        task.setArea(bike.getArea());
        task.setPriority(1);
        task.setDescription("车辆故障报修: " + request.getDescription());
        task.setCreateTime(LocalDateTime.now());
        task.setTimeoutTime(LocalDateTime.now().plusHours(6));
        dataStore.tasks.put(task.getId(), task);
        dataStore.taskPool.offer(task);

        return bike;
    }

    public Bike updateBattery(String bikeId, int batteryLevel) {
        Bike bike = dataStore.bikes.get(bikeId);
        if (bike == null) {
            throw new RuntimeException("车辆不存在");
        }

        bike.setBatteryLevel(batteryLevel);
        bike.setUpdateTime(LocalDateTime.now());

        if (batteryLevel <= 20 && bike.getStatus() == BikeStatus.AVAILABLE) {
            bike.setStatus(BikeStatus.LOW_BATTERY);
        } else if (batteryLevel > 20 && bike.getStatus() == BikeStatus.LOW_BATTERY) {
            bike.setStatus(BikeStatus.AVAILABLE);
        }

        return bike;
    }
}
