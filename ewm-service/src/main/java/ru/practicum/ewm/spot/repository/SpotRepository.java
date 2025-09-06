package ru.practicum.ewm.spot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.spot.model.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long>, SpotRepositoryExtension {
}
