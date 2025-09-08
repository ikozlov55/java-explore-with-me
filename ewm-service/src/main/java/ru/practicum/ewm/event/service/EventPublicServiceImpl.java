package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.core.exception.ValidationException;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventsSort;
import ru.practicum.ewm.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final ViewService viewService;

    @Override
    public Collection<EventFullDto> getEvents(String ip, String text, List<Long> categories,
                                              Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              boolean onlyAvailable, EventsSort sort, int from, int size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            String msg = String.format("Invalid date interval: %s - %s", rangeStart, rangeEnd);
            throw new ValidationException(msg);
        }
        viewService.eventListView(ip);
        Boolean onlyFuture = rangeStart == null && rangeEnd == null;
        List<EventFullDto> events = eventRepository.getEvents(text, null, List.of(EventState.PUBLISHED),
                        categories, paid, rangeStart, rangeEnd, onlyFuture, onlyAvailable, sort, from, size)
                .stream()
                .map(e -> EventMapper.toFullDto(e, viewService.getEventViews(e.getId())))
                .toList();
        if (sort == EventsSort.VIEWS) {
            events = events.stream()
                    .sorted(Comparator.comparing(EventFullDto::views).reversed())
                    .toList();
        }
        return events;
    }

    @Override
    public EventFullDto getEvent(String ip, long eventId) {
        viewService.eventViewHit(eventId, ip);
        Event event = eventRepository.findByIdOrThrow(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            String msg = String.format("Event with id=%d was not found", eventId);
            throw new NotFoundException(msg);
        }
        return EventMapper.toFullDto(event, viewService.getEventViews(eventId));
    }
}
