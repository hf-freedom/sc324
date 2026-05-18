package com.bike.ops.controller;

import com.bike.ops.model.AreaStatistics;
import com.bike.ops.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/areas")
    public List<AreaStatistics> getAreaStatistics() {
        return statisticsService.getAllStatistics();
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return statisticsService.getDashboardData();
    }
}
