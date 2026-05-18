package com.bike.ops.common;

import com.bike.ops.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStore {

    public final Map<String, Bike> bikes = new ConcurrentHashMap<>();
    public final Map<String, ParkingSpot> parkingSpots = new ConcurrentHashMap<>();
    public final Map<String, MaintenanceWorker> workers = new ConcurrentHashMap<>();
    public final Map<String, MaintenanceTask> tasks = new ConcurrentHashMap<>();
    public final Map<String, AbnormalRecord> abnormalRecords = new ConcurrentHashMap<>();
    public final Map<String, AreaStatistics> areaStatisticsMap = new ConcurrentHashMap<>();

    public final Queue<MaintenanceTask> taskPool = new LinkedList<>();
    public final List<String> areas = Arrays.asList("A区", "B区", "C区", "D区", "E区");

    public DataStore() {
        initData();
    }

    private void initData() {
        for (int i = 1; i <= 5; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.setId("PS" + i);
            spot.setName(areas.get(i - 1) + "停车点" + i);
            spot.setLongitude(116.3 + i * 0.01);
            spot.setLatitude(39.9 + i * 0.01);
            spot.setCapacity(10);
            spot.setArea(areas.get(i - 1));
            spot.setBikeIds(new ArrayList<>());
            spot.setCreateTime(java.time.LocalDateTime.now());
            spot.setUpdateTime(java.time.LocalDateTime.now());
            parkingSpots.put(spot.getId(), spot);
        }

        for (int i = 1; i <= 20; i++) {
            Bike bike = new Bike();
            bike.setId("B" + i);
            bike.setBikeNo("Bike-" + i);
            bike.setBatteryLevel(20 + (int) (Math.random() * 80));
            bike.setStatus(bike.getBatteryLevel() < 20 ?
                    com.bike.ops.model.enums.BikeStatus.LOW_BATTERY :
                    com.bike.ops.model.enums.BikeStatus.AVAILABLE);
            int spotIdx = (i - 1) % 5;
            String spotId = "PS" + (spotIdx + 1);
            bike.setParkingSpotId(spotId);
            bike.setArea(areas.get(spotIdx));
            bike.setLongitude(parkingSpots.get(spotId).getLongitude() + Math.random() * 0.001);
            bike.setLatitude(parkingSpots.get(spotId).getLatitude() + Math.random() * 0.001);
            bike.setCreateTime(java.time.LocalDateTime.now());
            bike.setUpdateTime(java.time.LocalDateTime.now());
            bikes.put(bike.getId(), bike);
            parkingSpots.get(spotId).getBikeIds().add(bike.getId());
        }

        for (int i = 1; i <= 5; i++) {
            MaintenanceWorker worker = new MaintenanceWorker();
            worker.setId("W" + i);
            worker.setName("运维员" + i);
            worker.setPhone("1380000000" + i);
            worker.setArea(areas.get(i - 1));
            worker.setAssignedTaskIds(new ArrayList<>());
            worker.setTaskLimit(5);
            worker.setIsOnline(true);
            worker.setCreateTime(java.time.LocalDateTime.now());
            worker.setUpdateTime(java.time.LocalDateTime.now());
            workers.put(worker.getId(), worker);
        }
    }
}
