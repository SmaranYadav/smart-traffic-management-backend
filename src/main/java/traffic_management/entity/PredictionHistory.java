package traffic_management.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "prediction_history")
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double vehicleSpeed;
    private double trafficDensity;
    private double accidentCount;
    private String riskLevel;

    private LocalDateTime createdAt;

    public PredictionHistory() {}

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public double getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(double value) {
        vehicleSpeed = value;
    }

    public double getTrafficDensity() {
        return trafficDensity;
    }

    public void setTrafficDensity(double value) {
        trafficDensity = value;
    }

    public double getAccidentCount() {
        return accidentCount;
    }

    public void setAccidentCount(double value) {
        accidentCount = value;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String value) {
        riskLevel = value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
