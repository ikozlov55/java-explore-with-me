package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, RequestRepositoryExtension {
    Collection<Request> findByEventIdAndEventInitiatorId(long eventId, long initiatorId);

    Collection<Request> findByEventIdAndEventInitiatorIdAndIdIn(long eventId, long initiatorId, List<Long> ids);

    Request findByIdAndRequesterId(long id, long requesterId);

    Collection<Request> findByRequesterId(long requesterId);

    Request findByRequesterIdAndEventId(long requesterId, long eventId);
}
