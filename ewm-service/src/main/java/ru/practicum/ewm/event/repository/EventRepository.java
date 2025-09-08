package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryExtension {
    Page<Event> findByInitiatorId(long userId, Pageable page);

    Event findByIdAndInitiatorId(long userId, long eventId);

    @Query(value = """
            SELECT e.*,
                     (SELECT COUNT(p.id)
                        FROM participation_requests p
                       WHERE p.event_id = id
                         AND p.status = 'CONFIRMED') as confirmedRequests
              FROM events e
             WHERE ST_DWithin(
                     ST_MakePoint(e.lat, e.lon),
                     ST_SetSRID(ST_MakePoint(:lat, :lon), 4326)::geography,
                   	 :radius
                   )
               AND e.state = 'PUBLISHED'
            """, nativeQuery = true)
    List<Event> findEventsInRadius(@Param("lat") double lat,
                                   @Param("lon") double lon,
                                   @Param("radius") int radius);

    List<Event> findByIdIn(List<Long> eventIds);

}
