package traffic_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import traffic_management.entity.TrafficData;
import traffic_management.ml.RandomForestModel;
import traffic_management.service.TrafficDataService;

@RestController
@RequestMapping("/api/traffic")
@CrossOrigin(origins = "*")
public class TrafficDataController {

    private final TrafficDataService service;
    private final RandomForestModel randomForestModel;

    public TrafficDataController(
            TrafficDataService service,
            RandomForestModel randomForestModel) {

        this.service = service;
        this.randomForestModel = randomForestModel;
    }

    // GET all traffic data
    @GetMapping
    public List<TrafficData> getAllTrafficData() {
        return service.getAllTrafficData();
    }

    // GET traffic data by ID
    @GetMapping("/{id}")
    public ResponseEntity<TrafficData> getTrafficDataById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getTrafficDataById(id)
        );
    }

    // CREATE traffic data
    @PostMapping
    public ResponseEntity<TrafficData> createTrafficData(
            @RequestBody TrafficData trafficData) {

        return ResponseEntity.ok(
                service.saveTrafficData(trafficData)
        );
    }

    // UPDATE traffic data
    @PutMapping("/{id}")
    public ResponseEntity<TrafficData> updateTrafficData(
            @PathVariable Long id,
            @RequestBody TrafficData trafficData) {

        return ResponseEntity.ok(
                service.updateTrafficData(id, trafficData)
        );
    }

    // DELETE traffic data
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrafficData(
            @PathVariable Long id) {

        service.deleteTrafficData(id);

        return ResponseEntity.ok(
                "Traffic data deleted successfully"
        );
    }

    // RANDOM FOREST TRAFFIC RISK PREDICTION
    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictTraffic(
            @RequestBody Map<String, Double> input) {

        double vehicleSpeed =
                input.get("vehicleSpeed");

        double trafficDensity =
                input.get("trafficDensity");

        double accidentCount =
                input.get("accidentCount");

        String riskLevel =
                randomForestModel.predict(
                        vehicleSpeed,
                        trafficDensity,
                        accidentCount
                );

        Map<String, Object> response = Map.of(
                "vehicleSpeed", vehicleSpeed,
                "trafficDensity", trafficDensity,
                "accidentCount", accidentCount,
                "riskLevel", riskLevel
        );

        return ResponseEntity.ok(response);
    }
}