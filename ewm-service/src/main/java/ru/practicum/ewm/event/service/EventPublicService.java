package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventsSort;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventPublicService {
    Collection<EventFullDto> getEvents(String ip, String text, List<Long> categories, Boolean paid,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                                       EventsSort sort, int from, int size);

    EventFullDto getEvent(String ip, long eventId);
}
