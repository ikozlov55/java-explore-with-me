package ru.practicum.ewm.stats.service.hit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long>,
        EndpointHitRepositoryExtension {
}
