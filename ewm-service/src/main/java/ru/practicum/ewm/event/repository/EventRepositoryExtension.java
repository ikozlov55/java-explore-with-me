package ru.practicum.ewm.event.repository;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventsSort;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventRepositoryExtension {
    Event findByIdOrThrow(long eventId);

    Collection<Event> getEvents(String text, List<Long> users, List<EventState> states,
                                List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Boolean onlyFuture, Boolean onlyAvailable,
                                EventsSort sort, int from, int size);
}
