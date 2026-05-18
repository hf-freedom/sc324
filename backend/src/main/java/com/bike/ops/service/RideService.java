package com.bike.ops.service;

import com.bike.ops.common.DataStore;
import com.bike.ops.dto.RideEndRequest;
import com.bike.ops.model.*;
import com.bike.ops.model.enums.BikeStatus;
import com.bike.ops.model.enums.TaskStatus;
import com.bike.ops.model.enums.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RideService {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private TaskService taskService;

    public Bike endRide(RideEndRequest request) {
        Bike bike = dataStore.bikes.get(request.getBikeId());
        if (bike == null) {
            throw new RuntimeException("车辆不存在");
        }

        if (request.getParkingSpotId() != null) {
            ParkingSpot spot = dataStore.parkingSpots.get(request.getParkingSpotId());
            if (spot != null && spot.isFull()) {
                AbnormalRecord record = new AbnormalRecord();
                record.setId(UUID.randomUUID().toString());
                record.setBikeId(bike.getId());
                record.setParkingSpotId(spot.getId());
                record.setDescription("停车点容量已满，超容量停车");
                record.setArea(spot.getArea());
                record.setCreateTime(LocalDateTime.now());
                record.setResolved(false);
                dataStore.abnormalRecords.put(record.getId(), record);

                MaintenanceTask task = new MaintenanceTask();
                task.setId(UUID.randomUUID().toString());
                task.setTaskType(TaskType.ABNORMAL_PARKING);
                task.setStatus(TaskStatus.PENDING);
                task.setBikeId(bike.getId());
                task.setFromParkingSpotId(spot.getId());
                task.setArea(spot.getArea());
                task.setPriority(2);
                task.setDescription("处理超容量停车异常");
                task.setCreateTime(LocalDateTime.now());
                task.setTimeoutTime(LocalDateTime.now().plusHours(2));
                dataStore.tasks.put(task.getId(), task);
                dataStore.taskPool.offer(task);
            } else if (spot != null) {
                if (bike.getParkingSpotId() != null && !bike.getParkingSpotId().equals(spot.getId())) {
                    ParkingSpot oldSpot = dataStore.parkingSpots.get(bike.getParkingSpotId());
                    if (oldSpot != null) {
                        oldSpot.getBikeIds().remove(bike.getId());
                        oldSpot.setUpdateTime(LocalDateTime.now());
                    }
                }
                spot.getBikeIds().add(bike.getId());
                spot.setUpdateTime(LocalDateTime.now());
                bike.setParkingSpotId(spot.getId());
                bike.setArea(spot.getArea());
            }
        }

        bike.setLongitude(request.getLongitude());
        bike.setLatitude(request.getLatitude());
        bike.setBatteryLevel(request.getBatteryLevel());
        bike.setLastRideEndTime(LocalDateTime.now());
        bike.setUpdateTime(LocalDateTime.now());

        if (request.getBatteryLevel() <= 20) {
            bike.setStatus(BikeStatus.LOW_BATTERY);
            taskService.createBatterySwapTask(bike);
        } else {
            bike.setStatus(BikeStatus.AVAILABLE);
        }

        return bike;
    }
}
