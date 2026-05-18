package com.bike.ops.service;

import com.bike.ops.common.DataStore;
import com.bike.ops.model.Bike;
import com.bike.ops.model.MaintenanceTask;
import com.bike.ops.model.enums.BikeStatus;
import com.bike.ops.model.enums.TaskStatus;
import com.bike.ops.model.enums.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private DataStore dataStore;

    public void createBatterySwapTask(Bike bike) {
        MaintenanceTask existingTask = dataStore.tasks.values().stream()
                .filter(t -> t.getBikeId().equals(bike.getId())
                        && t.getTaskType() == TaskType.BATTERY_SWAP
                        && (t.getStatus() == TaskStatus.PENDING || t.getStatus() == TaskStatus.ASSIGNED))
                .findFirst().orElse(null);

        if (existingTask == null) {
            MaintenanceTask task = new MaintenanceTask();
            task.setId(UUID.randomUUID().toString());
            task.setTaskType(TaskType.BATTERY_SWAP);
            task.setStatus(TaskStatus.PENDING);
            task.setBikeId(bike.getId());
            task.setArea(bike.getArea());
            task.setPriority(bike.getBatteryLevel() <= 10 ? 1 : 2);
            task.setDescription("车辆电量低，需要换电");
            task.setCreateTime(LocalDateTime.now());
            task.setTimeoutTime(LocalDateTime.now().plusHours(4));
            dataStore.tasks.put(task.getId(), task);
            dataStore.taskPool.offer(task);
        }
    }

    public List<MaintenanceTask> getPendingTasks() {
        return dataStore.tasks.values().stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<MaintenanceTask> getTasksByArea(String area) {
        return dataStore.tasks.values().stream()
                .filter(t -> area == null || area.equals(t.getArea()))
                .collect(Collectors.toList());
    }

    public List<MaintenanceTask> getTasksByWorker(String workerId) {
        return dataStore.tasks.values().stream()
                .filter(t -> workerId.equals(t.getAssignedWorkerId()))
                .collect(Collectors.toList());
    }

    public MaintenanceTask completeTask(String taskId, String workerId) {
        MaintenanceTask task = dataStore.tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        if (!workerId.equals(task.getAssignedWorkerId())) {
            throw new RuntimeException("无权完成此任务");
        }

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompleteTime(LocalDateTime.now());

        if (task.getTaskType() == TaskType.BATTERY_SWAP) {
            Bike bike = dataStore.bikes.get(task.getBikeId());
            if (bike != null && bike.getStatus() == BikeStatus.LOW_BATTERY) {
                bike.setBatteryLevel(100);
                bike.setStatus(BikeStatus.AVAILABLE);
                bike.setUpdateTime(LocalDateTime.now());
            }
        } else if (task.getTaskType() == TaskType.MAINTENANCE) {
            Bike bike = dataStore.bikes.get(task.getBikeId());
            if (bike != null && bike.getStatus() == BikeStatus.MAINTENANCE) {
                bike.setStatus(BikeStatus.AVAILABLE);
                bike.setUpdateTime(LocalDateTime.now());
            }
        }

        return task;
    }

    public List<MaintenanceTask> getTimeoutTasks() {
        LocalDateTime now = LocalDateTime.now();
        return dataStore.tasks.values().stream()
                .filter(t -> (t.getStatus() == TaskStatus.PENDING || t.getStatus() == TaskStatus.ASSIGNED)
                        && t.getTimeoutTime() != null
                        && now.isAfter(t.getTimeoutTime()))
                .collect(Collectors.toList());
    }
}
