package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.service.EventPrivateService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventPrivateService eventService;

    @GetMapping("/{userId}/events")
    Collection<EventShortDto> getEvents(@PathVariable long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto createEvent(@PathVariable long userId,
                             @Valid @RequestBody NewEventDto eventDto) {
        return eventService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventFullDto getEvent(@PathVariable long userId, @PathVariable long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventFullDto updateEvent(@PathVariable long userId,
                             @PathVariable long eventId,
                             @Valid @RequestBody UpdateEventDto eventDto) {
        return eventService.updateEvent(userId, eventId, eventDto);
    }
}
