package traffic_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import traffic_management.entity.TrafficData;
import traffic_management.repository.TrafficDataRepository;

@Service
public class TrafficDataService {

    private final TrafficDataRepository repository;

    public TrafficDataService(TrafficDataRepository repository) {
        this.repository = repository;
    }

    // Get all traffic records
    public List<TrafficData> getAllTrafficData() {
        return repository.findAll();
    }

    // Get traffic record by ID
    public TrafficData getTrafficDataById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Traffic data not found"));
    }

    // Add new traffic record
    public TrafficData saveTrafficData(TrafficData trafficData) {
        return repository.save(trafficData);
    }

    // Update traffic record
    public TrafficData updateTrafficData(Long id, TrafficData trafficData) {

        TrafficData existingData = getTrafficDataById(id);

        existingData.setArea(trafficData.getArea());
        existingData.setLatitude(trafficData.getLatitude());
        existingData.setLongitude(trafficData.getLongitude());
        existingData.setVehicleSpeed(trafficData.getVehicleSpeed());
        existingData.setTrafficDensity(trafficData.getTrafficDensity());
        existingData.setAccidentCount(trafficData.getAccidentCount());

        return repository.save(existingData);
    }

    // Delete traffic record
    public void deleteTrafficData(Long id) {
        repository.deleteById(id);
    }
}