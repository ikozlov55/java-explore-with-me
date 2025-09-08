package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.core.exception.ConflictException;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ViewService viewService;

    @Override
    public Collection<EventShortDto> getEvents(long userId, int from, int size) {
        User user = userRepository.findByIdOrThrow(userId);
        Pageable page = PageRequest.of(from / size, size, Sort.by("eventDate"));
        return eventRepository.findByInitiatorId(user.getId(), page).stream()
                .map(e -> EventMapper.toShortDto(e, viewService.getEventViews(e.getId())))
                .toList();
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto eventDto) {
        User initiator = userRepository.findByIdOrThrow(userId);
        Category category = categoryRepository.findByIdOrThrow(eventDto.category());
        Event event = EventMapper.toModel(eventDto);
        event.setCategory(category);
        event.setInitiator(initiator);
        event = eventRepository.save(event);

        return EventMapper.toFullDto(event, category, initiator, 0);
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            String msg = String.format("Event with id=%d was not found", eventId);
            throw new NotFoundException(msg);
        }

        return EventMapper.toFullDto(event, viewService.getEventViews(eventId));
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventDto eventDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        if (event == null) {
            String msg = String.format("Event with id=%d was not found", eventId);
            throw new NotFoundException(msg);
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
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
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
            }
        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toFullDto(updatedEvent, viewService.getEventViews(eventId));
    }
}

