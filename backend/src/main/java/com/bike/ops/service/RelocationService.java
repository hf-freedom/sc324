package com.bike.ops.service;

import com.bike.ops.common.DataStore;
import com.bike.ops.model.*;
import com.bike.ops.model.enums.BikeStatus;
import com.bike.ops.model.enums.TaskStatus;
import com.bike.ops.model.enums.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelocationService {

    @Autowired
    private DataStore dataStore;

    public void generateRelocationTasks() {
        Map<String, List<ParkingSpot>> areaSpots = dataStore.parkingSpots.values().stream()
                .collect(Collectors.groupingBy(ParkingSpot::getArea));

        Map<String, Double> areaHeatScores = new HashMap<>();
        for (String area : dataStore.areas) {
            List<ParkingSpot> spots = areaSpots.getOrDefault(area, new ArrayList<>());
            int totalCapacity = spots.stream().mapToInt(ParkingSpot::getCapacity).sum();
            int totalBikes = spots.stream().mapToInt(ParkingSpot::getCurrentBikeCount).sum();
            double utilizationRate = totalCapacity > 0 ? (double) totalBikes / totalCapacity : 0;
            areaHeatScores.put(area, utilizationRate);
        }

        List<Map.Entry<String, Double>> sortedAreas = areaHeatScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        if (sortedAreas.size() < 2) return;

        String hotArea = sortedAreas.get(0).getKey();
        double hotScore = sortedAreas.get(0).getValue();
        String coldArea = sortedAreas.get(sortedAreas.size() - 1).getKey();
        double coldScore = sortedAreas.get(sortedAreas.size() - 1).getValue();

        if (hotScore > 0.8 && coldScore < 0.3) {
            List<ParkingSpot> hotSpots = areaSpots.get(hotArea);
            List<ParkingSpot> coldSpots = areaSpots.get(coldArea);

            ParkingSpot overloadedSpot = hotSpots.stream()
                    .max(Comparator.comparingInt(ParkingSpot::getCurrentBikeCount))
                    .orElse(null);

            ParkingSpot underloadedSpot = coldSpots.stream()
                    .min(Comparator.comparingInt(ParkingSpot::getCurrentBikeCount))
                    .orElse(null);

            if (overloadedSpot != null && underloadedSpot != null) {
                int bikesToMove = Math.min(3, overloadedSpot.getCurrentBikeCount() - overloadedSpot.getCapacity() / 2);
                bikesToMove = Math.min(bikesToMove, underloadedSpot.getCapacity() - underloadedSpot.getCurrentBikeCount());

                if (bikesToMove > 0) {
                    List<String> availableBikeIds = overloadedSpot.getBikeIds().stream()
                            .filter(bikeId -> {
                                Bike bike = dataStore.bikes.get(bikeId);
                                return bike != null && bike.getStatus() == BikeStatus.AVAILABLE;
                            })
                            .limit(bikesToMove)
                            .collect(Collectors.toList());

                    for (String bikeId : availableBikeIds) {
                        boolean existingTask = dataStore.tasks.values().stream()
                                .anyMatch(t -> t.getBikeId().equals(bikeId)
                                        && t.getTaskType() == TaskType.RELOCATION
                                        && (t.getStatus() == TaskStatus.PENDING || t.getStatus() == TaskStatus.ASSIGNED));

                        if (!existingTask) {
                            MaintenanceTask task = new MaintenanceTask();
                            task.setId(UUID.randomUUID().toString());
                            task.setTaskType(TaskType.RELOCATION);
                            task.setStatus(TaskStatus.PENDING);
                            task.setBikeId(bikeId);
                            task.setFromParkingSpotId(overloadedSpot.getId());
                            task.setToParkingSpotId(underloadedSpot.getId());
                            task.setArea(hotArea);
                            task.setPriority(2);
                            task.setDescription("从" + overloadedSpot.getName() + "调拨到" + underloadedSpot.getName());
                            task.setCreateTime(LocalDateTime.now());
                            task.setTimeoutTime(LocalDateTime.now().plusHours(3));
                            dataStore.tasks.put(task.getId(), task);
                            dataStore.taskPool.offer(task);
                        }
                    }
                }
            }
        }
    }
}
