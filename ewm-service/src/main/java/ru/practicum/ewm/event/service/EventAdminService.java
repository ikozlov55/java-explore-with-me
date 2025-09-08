package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventAdminService {
    Collection<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEvent(long eventId, UpdateEventDto eventDto);
}
