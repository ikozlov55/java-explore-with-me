package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.core.exception.ForbiddenException;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {
    public static final String PUBLISH_ERROR_TEMPLATE = "Cannot publish the event because it's not in the right state: %s";
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ViewService viewService;

    @Override
    public Collection<EventFullDto> getEvents(List<Long> users, List<EventState> states,
                                              List<Long> categories, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, int from, int size) {
        return eventRepository.getEvents(null, users, states, categories, null,
                        rangeStart, rangeEnd, null, null, null,
                        from, size).stream()
                .map(e -> EventMapper.toFullDto(e, null, null, viewService.getEventViews(e.getId())))
                .toList();
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventDto eventDto) {
        Event event = eventRepository.findByIdOrThrow(eventId);

        if (eventDto.title() != null) {
            event.setTitle(eventDto.title());
        }
        if (eventDto.description() != null) {
            event.setDescription(eventDto.description());
        }
        if (eventDto.annotation() != null) {
            event.setAnnotation(eventDto.annotation());
        }
        if (eventDto.eventDate() != null) {
            event.setEventDate(eventDto.eventDate());
        }
        if (eventDto.requestModeration() != null) {
            event.setRequestModeration(eventDto.requestModeration());
        }
        if (eventDto.participantLimit() != null) {
            event.setParticipantLimit(eventDto.participantLimit());
        }
        if (eventDto.paid() != null) {
            event.setPaid(eventDto.paid());
        }
        if (eventDto.location() != null) {
            event.setLat(eventDto.location().lat());
            event.setLon(eventDto.location().lon());
        }
        if (eventDto.category() != null) {
            Category category = categoryRepository.findByIdOrThrow(eventDto.category());
            event.setCategory(category);
        }
        if (eventDto.stateAction() != null) {
            switch (eventDto.stateAction()) {
                case PUBLISH_EVENT -> {
                    if (event.getState() != EventState.PENDING) {
                        String msg = String.format(PUBLISH_ERROR_TEMPLATE, event.getState());
                        throw new ForbiddenException(msg);
                    }
                    if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                        throw new ForbiddenException(String.format(PUBLISH_ERROR_TEMPLATE,
                                "event date must be at least 1 hour in future"));
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
                case REJECT_EVENT -> {
                    if (event.getState() != EventState.PENDING) {
                        String msg = String.format(PUBLISH_ERROR_TEMPLATE, event.getState());
                        throw new ForbiddenException(msg);
                    }
                    event.setState(EventState.CANCELED);
                }
            }
        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toFullDto(updatedEvent, null, null, viewService.getEventViews(eventId));
    }
}
