package com.bike.ops.controller;

import com.bike.ops.common.DataStore;
import com.bike.ops.model.MaintenanceTask;
import com.bike.ops.model.ParkingSpot;
import com.bike.ops.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private DataStore dataStore;

    @GetMapping
    public List<MaintenanceTask> getTasks(@RequestParam(required = false) String area,
                                          @RequestParam(required = false) String workerId) {
        if (workerId != null) {
            return taskService.getTasksByWorker(workerId);
        }
        return taskService.getTasksByArea(area);
    }

    @GetMapping("/pending")
    public List<MaintenanceTask> getPendingTasks() {
        return taskService.getPendingTasks();
    }

    @PostMapping("/{id}/complete")
    public MaintenanceTask completeTask(@PathVariable String id, @RequestBody Map<String, String> request) {
        return taskService.completeTask(id, request.get("workerId"));
    }

    @GetMapping("/timeout")
    public List<MaintenanceTask> getTimeoutTasks() {
        return taskService.getTimeoutTasks();
    }
}
