package com.bike.ops.controller;

import com.bike.ops.common.DataStore;
import com.bike.ops.model.AbnormalRecord;
import com.bike.ops.model.MaintenanceWorker;
import com.bike.ops.model.ParkingSpot;
import com.bike.ops.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SystemController {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private WorkerService workerService;

    @GetMapping("/workers")
    public List<MaintenanceWorker> getWorkers() {
        return workerService.getAllWorkers();
    }

    @GetMapping("/workers/{id}")
    public MaintenanceWorker getWorker(@PathVariable String id) {
        return workerService.getWorkerById(id);
    }

    @GetMapping("/parking-spots")
    public List<ParkingSpot> getParkingSpots(@RequestParam(required = false) String area) {
        return dataStore.parkingSpots.values().stream()
                .filter(s -> area == null || area.equals(s.getArea()))
                .collect(Collectors.toList());
    }

    @GetMapping("/parking-spots/{id}")
    public ParkingSpot getParkingSpot(@PathVariable String id) {
        return dataStore.parkingSpots.get(id);
    }

    @GetMapping("/abnormal-records")
    public List<AbnormalRecord> getAbnormalRecords(@RequestParam(required = false) Boolean resolved) {
        return dataStore.abnormalRecords.values().stream()
                .filter(r -> resolved == null || resolved.equals(r.getResolved()))
                .collect(Collectors.toList());
    }

    @PostMapping("/abnormal-records/{id}/resolve")
    public AbnormalRecord resolveAbnormalRecord(@PathVariable String id) {
        AbnormalRecord record = dataStore.abnormalRecords.get(id);
        if (record != null) {
            record.setResolved(true);
            record.setResolveTime(java.time.LocalDateTime.now());
        }
        return record;
    }

    @GetMapping("/areas")
    public List<String> getAreas() {
        return dataStore.areas;
    }
}
