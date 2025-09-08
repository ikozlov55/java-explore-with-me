package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;

import java.util.Collection;

public interface EventPrivateService {
    Collection<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto eventDto);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventDto eventDto);
}
