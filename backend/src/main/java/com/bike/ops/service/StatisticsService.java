package com.bike.ops.service;

import com.bike.ops.common.DataStore;
import com.bike.ops.model.*;
import com.bike.ops.model.enums.BikeStatus;
import com.bike.ops.model.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private DataStore dataStore;

    public void calculateAreaStatistics() {
        for (String area : dataStore.areas) {
            AreaStatistics stats = new AreaStatistics();
            stats.setArea(area);
            stats.setStatisticsTime(LocalDateTime.now());

            List<Bike> areaBikes = dataStore.bikes.values().stream()
                    .filter(b -> area.equals(b.getArea()))
                    .collect(Collectors.toList());

            stats.setTotalBikes(areaBikes.size());
            stats.setAvailableBikes((int) areaBikes.stream()
                    .filter(b -> b.getStatus() == BikeStatus.AVAILABLE).count());
            stats.setLowBatteryBikes((int) areaBikes.stream()
                    .filter(b -> b.getStatus() == BikeStatus.LOW_BATTERY).count());
            stats.setInUseBikes((int) areaBikes.stream()
                    .filter(b -> b.getStatus() == BikeStatus.IN_USE).count());
            stats.setMaintenanceBikes((int) areaBikes.stream()
                    .filter(b -> b.getStatus() == BikeStatus.MAINTENANCE).count());

            List<ParkingSpot> areaSpots = dataStore.parkingSpots.values().stream()
                    .filter(s -> area.equals(s.getArea()))
                    .collect(Collectors.toList());

            stats.setParkingSpots(areaSpots.size());
            stats.setOverCapacitySpots((int) areaSpots.stream().filter(ParkingSpot::isFull).count());

            int pendingTasks = (int) dataStore.tasks.values().stream()
                    .filter(t -> area.equals(t.getArea()) && t.getStatus() == TaskStatus.PENDING)
                    .count();
            stats.setPendingTasks(pendingTasks);

            dataStore.areaStatisticsMap.put(area, stats);
        }
    }

    public List<AreaStatistics> getAllStatistics() {
        return dataStore.areaStatisticsMap.values().stream().collect(Collectors.toList());
    }

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("totalBikes", dataStore.bikes.size());
        data.put("availableBikes", (int) dataStore.bikes.values().stream()
                .filter(b -> b.getStatus() == BikeStatus.AVAILABLE).count());
        data.put("lowBatteryBikes", (int) dataStore.bikes.values().stream()
                .filter(b -> b.getStatus() == BikeStatus.LOW_BATTERY).count());
        data.put("maintenanceBikes", (int) dataStore.bikes.values().stream()
                .filter(b -> b.getStatus() == BikeStatus.MAINTENANCE).count());
        data.put("totalParkingSpots", dataStore.parkingSpots.size());
        data.put("fullParkingSpots", (int) dataStore.parkingSpots.values().stream()
                .filter(ParkingSpot::isFull).count());
        data.put("totalWorkers", dataStore.workers.size());
        data.put("onlineWorkers", (int) dataStore.workers.values().stream()
                .filter(MaintenanceWorker::getIsOnline).count());
        data.put("totalTasks", dataStore.tasks.size());
        data.put("pendingTasks", (int) dataStore.tasks.values().stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING).count());
        data.put("completedTasks", (int) dataStore.tasks.values().stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED).count());
        data.put("abnormalRecords", dataStore.abnormalRecords.size());
        return data;
    }
}
