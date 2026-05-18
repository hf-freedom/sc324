package com.bike.ops.model;

import com.bike.ops.model.enums.TaskStatus;
import com.bike.ops.model.enums.TaskType;
import java.time.LocalDateTime;

public class MaintenanceTask {
    private String id;
    private TaskType taskType;
    private TaskStatus status;
    private String bikeId;
    private String fromParkingSpotId;
    private String toParkingSpotId;
    private String area;
    private Integer priority;
    private String assignedWorkerId;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime assignTime;
    private LocalDateTime completeTime;
    private LocalDateTime timeoutTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public TaskType getTaskType() { return taskType; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public String getFromParkingSpotId() { return fromParkingSpotId; }
    public void setFromParkingSpotId(String fromParkingSpotId) { this.fromParkingSpotId = fromParkingSpotId; }
    public String getToParkingSpotId() { return toParkingSpotId; }
    public void setToParkingSpotId(String toParkingSpotId) { this.toParkingSpotId = toParkingSpotId; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getAssignedWorkerId() { return assignedWorkerId; }
    public void setAssignedWorkerId(String assignedWorkerId) { this.assignedWorkerId = assignedWorkerId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getAssignTime() { return assignTime; }
    public void setAssignTime(LocalDateTime assignTime) { this.assignTime = assignTime; }
    public LocalDateTime getCompleteTime() { return completeTime; }
    public void setCompleteTime(LocalDateTime completeTime) { this.completeTime = completeTime; }
    public LocalDateTime getTimeoutTime() { return timeoutTime; }
    public void setTimeoutTime(LocalDateTime timeoutTime) { this.timeoutTime = timeoutTime; }
}
