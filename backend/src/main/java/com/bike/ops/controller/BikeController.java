package com.bike.ops.controller;

import com.bike.ops.common.DataStore;
import com.bike.ops.dto.RideEndRequest;
import com.bike.ops.model.Bike;
import com.bike.ops.service.BikeService;
import com.bike.ops.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bikes")
@CrossOrigin(origins = "*")
public class BikeController {

    @Autowired
    private BikeService bikeService;

    @Autowired
    private RideService rideService;

    @Autowired
    private DataStore dataStore;

    @GetMapping
    public List<Bike> getBikes(@RequestParam(required = false) String area,
                               @RequestParam(required = false) String status) {
        List<Bike> bikes = bikeService.getBikesByArea(area);
        if (status != null) {
            bikes = bikes.stream()
                    .filter(b -> b.getStatus().name().equals(status))
                    .collect(java.util.stream.Collectors.toList());
        }
        return bikes;
    }

    @PostMapping("/start-ride")
    public Bike startRide(@RequestBody Map<String, String> request) {
        String bikeId = request.get("bikeId");
        return bikeService.startRide(bikeId);
    }

    @PostMapping("/end-ride")
    public Bike endRide(@RequestBody RideEndRequest request) {
        return rideService.endRide(request);
    }

    @PostMapping("/report-fault")
    public Bike reportFault(@RequestBody Map<String, String> request) {
        com.bike.ops.dto.FaultReportRequest req = new com.bike.ops.dto.FaultReportRequest();
        req.setBikeId(request.get("bikeId"));
        req.setDescription(request.get("description"));
        return bikeService.reportFault(req);
    }

    @PutMapping("/{id}/battery")
    public Bike updateBattery(@PathVariable String id, @RequestBody Map<String, Integer> request) {
        return bikeService.updateBattery(id, request.get("batteryLevel"));
    }

    @GetMapping("/low-battery")
    public List<Bike> getLowBatteryBikes() {
        return bikeService.getLowBatteryBikes();
    }

    @GetMapping("/available")
    public List<Bike> getAvailableBikes() {
        return bikeService.getAvailableBikes();
    }
}
