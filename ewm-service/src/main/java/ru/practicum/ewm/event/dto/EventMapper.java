package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;

public final class EventMapper {
    private EventMapper() {
    }

    public static Event toModel(NewEventDto eventDto) {
        return new Event(null,
                eventDto.title(),
                eventDto.description(),
                eventDto.annotation(),
                eventDto.eventDate(),
                eventDto.requestModeration() != null ? eventDto.requestModeration() : true,
                eventDto.participantLimit() != null ? eventDto.participantLimit() : 0,
                0L,
                eventDto.paid() != null ? eventDto.paid() : false,
                eventDto.location().lat(),
                eventDto.location().lon(),
                EventState.PENDING,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static EventFullDto toFullDto(Event event, long views) {
        return toFullDto(event, null, null, views);
    }

    public static EventFullDto toFullDto(Event event, Category category, User user, long views) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                event.getState(),
                event.getEventDate(),
                event.getRequestModeration(),
                event.getParticipantLimit(),
                event.getConfirmedRequests(),
                event.getPaid(),
                new LocationDto(event.getLat(), event.getLon()),
                category != null ? CategoryMapper.toDto(category) : CategoryMapper.toDto(event.getCategory()),
                user != null ? UserMapper.toDto(user) : UserMapper.toDto(event.getInitiator()),
                views,
                event.getCreatedOn(),
                event.getPublishedOn()
        );
    }

    public static EventShortDto toShortDto(Event event, long views) {
        return new EventShortDto(event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getEventDate(),
                event.getConfirmedRequests(),
                event.getPaid(),
                CategoryMapper.toDto(event.getCategory()),
                UserMapper.toDto(event.getInitiator()),
                views);
    }
}

