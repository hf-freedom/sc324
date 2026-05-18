package com.bike.ops.service;

import com.bike.ops.common.DataStore;
import com.bike.ops.model.MaintenanceTask;
import com.bike.ops.model.MaintenanceWorker;
import com.bike.ops.model.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    @Autowired
    private DataStore dataStore;

    public List<MaintenanceWorker> getAllWorkers() {
        return dataStore.workers.values().stream().collect(Collectors.toList());
    }

    public MaintenanceWorker getWorkerById(String id) {
        return dataStore.workers.get(id);
    }

    public void assignTasks() {
        List<MaintenanceWorker> availableWorkers = dataStore.workers.values().stream()
                .filter(MaintenanceWorker::canAcceptMoreTasks)
                .collect(Collectors.toList());

        List<MaintenanceTask> pendingTasks = dataStore.tasks.values().stream()
                .filter(t -> t.getStatus() == TaskStatus.PENDING)
                .sorted(Comparator.comparingInt(MaintenanceTask::getPriority)
                        .thenComparing(MaintenanceTask::getCreateTime))
                .collect(Collectors.toList());

        for (MaintenanceTask task : pendingTasks) {
            MaintenanceWorker bestWorker = availableWorkers.stream()
                    .filter(w -> w.getArea().equals(task.getArea()) && w.canAcceptMoreTasks())
                    .min(Comparator.comparingInt(MaintenanceWorker::getCurrentTaskCount))
                    .orElse(null);

            if (bestWorker == null) {
                bestWorker = availableWorkers.stream()
                        .filter(MaintenanceWorker::canAcceptMoreTasks)
                        .min(Comparator.comparingInt(MaintenanceWorker::getCurrentTaskCount))
                        .orElse(null);
            }

            if (bestWorker != null) {
                task.setAssignedWorkerId(bestWorker.getId());
                task.setStatus(TaskStatus.ASSIGNED);
                task.setAssignTime(LocalDateTime.now());
                bestWorker.getAssignedTaskIds().add(task.getId());
                bestWorker.setUpdateTime(LocalDateTime.now());
            }
        }
    }

    public void reassignTimeoutTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<MaintenanceTask> timeoutTasks = dataStore.tasks.values().stream()
                .filter(t -> (t.getStatus() == TaskStatus.ASSIGNED)
                        && t.getTimeoutTime() != null
                        && now.isAfter(t.getTimeoutTime()))
                .collect(Collectors.toList());

        for (MaintenanceTask task : timeoutTasks) {
            if (task.getAssignedWorkerId() != null) {
                MaintenanceWorker oldWorker = dataStore.workers.get(task.getAssignedWorkerId());
                if (oldWorker != null) {
                    oldWorker.getAssignedTaskIds().remove(task.getId());
                    oldWorker.setUpdateTime(LocalDateTime.now());
                }
            }
            task.setStatus(TaskStatus.TIMEOUT);
            task.setAssignedWorkerId(null);
            task.setAssignTime(null);

            MaintenanceTask newTask = new MaintenanceTask();
            newTask.setId(java.util.UUID.randomUUID().toString());
            newTask.setTaskType(task.getTaskType());
            newTask.setStatus(TaskStatus.PENDING);
            newTask.setBikeId(task.getBikeId());
            newTask.setFromParkingSpotId(task.getFromParkingSpotId());
            newTask.setToParkingSpotId(task.getToParkingSpotId());
            newTask.setArea(task.getArea());
            newTask.setPriority(task.getPriority());
            newTask.setDescription("(重新分配)" + task.getDescription());
            newTask.setCreateTime(LocalDateTime.now());
            newTask.setTimeoutTime(LocalDateTime.now().plusHours(2));
            dataStore.tasks.put(newTask.getId(), newTask);
            dataStore.taskPool.offer(newTask);
        }

        assignTasks();
    }
}
