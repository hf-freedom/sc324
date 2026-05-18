package com.bike.ops.model;

import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceWorker {
    private String id;
    private String name;
    private String phone;
    private String area;
    private List<String> assignedTaskIds;
    private Integer taskLimit;
    private Boolean isOnline;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public int getCurrentTaskCount() {
        return assignedTaskIds != null ? assignedTaskIds.size() : 0;
    }

    public boolean canAcceptMoreTasks() {
        return isOnline && getCurrentTaskCount() < taskLimit;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public List<String> getAssignedTaskIds() { return assignedTaskIds; }
    public void setAssignedTaskIds(List<String> assignedTaskIds) { this.assignedTaskIds = assignedTaskIds; }
    public Integer getTaskLimit() { return taskLimit; }
    public void setTaskLimit(Integer taskLimit) { this.taskLimit = taskLimit; }
    public Boolean getIsOnline() { return isOnline; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
