package ru.practicum.ewm.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventAdminService;
import ru.practicum.ewm.stats.dto.Constants;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventAdminService eventService;

    @GetMapping
    Collection<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                       @RequestParam(required = false) List<EventState> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
                                       LocalDateTime rangeStart,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
                                       LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable long eventId, @Valid @RequestBody UpdateEventDto eventDto) {
        return eventService.updateEvent(eventId, eventDto);
    }
}
