package traffic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import traffic_management.entity.TrafficData;

@Repository
public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {

}