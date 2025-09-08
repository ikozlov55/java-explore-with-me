package ru.practicum.ewm.event.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventsSort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventRepositoryExtensionImpl implements EventRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Event findByIdOrThrow(long eventId) {
        return entityManager.createQuery("SELECT e FROM Event e WHERE e.id = :eventId", Event.class)
                .setParameter("eventId", eventId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("Event with id=%d was not found", eventId);
                    return new NotFoundException(msg);
                });
    }

    @Override
    public Collection<Event> getEvents(String text, List<Long> users, List<EventState> states,
                                       List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Boolean onlyFuture, Boolean onlyAvailable,
                                       EventsSort sort, int from, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            String like = String.format("%%%s%%", text.toLowerCase());
            Predicate annotationLike = builder.like(builder.lower(root.get("annotation")), like);
            Predicate descriptionLike = builder.like(builder.lower(root.get("description")), like);
            predicates.add(builder.or(annotationLike, descriptionLike));
        }
        if (users != null && !users.isEmpty()) {
            predicates.add(root.get("initiator").get("id").in(users));
        }
        if (states != null && !states.isEmpty()) {
            predicates.add(root.get("state").in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categories));
        }
        if (paid != null) {
            predicates.add(builder.equal(root.get("paid"), paid));
        }
        if (rangeStart != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
        }
        if (onlyFuture != null && onlyFuture) {
            predicates.add(builder.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }
        if (onlyAvailable != null && onlyAvailable) {
            Predicate noLimit = builder.equal(root.get("participantLimit"), 0);
            Predicate limitNotReached = builder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"));
            predicates.add(builder.or(limitNotReached, noLimit));
        }
        query.where(predicates.toArray(Predicate[]::new));

        if (sort == EventsSort.EVENT_DATE) {
            query.orderBy(builder.desc(root.get("eventDate")));
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);

        return typedQuery.getResultList();
    }
}
