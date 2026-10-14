package traffic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import traffic_management.entity.PredictionHistory;

public interface PredictionHistoryRepository
        extends JpaRepository<PredictionHistory, Long> {
}
