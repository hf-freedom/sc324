package com.bike.ops.task;

import com.bike.ops.service.RelocationService;
import com.bike.ops.service.StatisticsService;
import com.bike.ops.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private RelocationService relocationService;

    @Scheduled(fixedRate = 30000)
    public void calculateStatistics() {
        statisticsService.calculateAreaStatistics();
    }

    @Scheduled(fixedRate = 60000)
    public void assignTasks() {
        workerService.assignTasks();
    }

    @Scheduled(fixedRate = 120000)
    public void checkTimeoutTasks() {
        workerService.reassignTimeoutTasks();
    }

    @Scheduled(fixedRate = 180000)
    public void generateRelocationTasks() {
        relocationService.generateRelocationTasks();
    }
}
