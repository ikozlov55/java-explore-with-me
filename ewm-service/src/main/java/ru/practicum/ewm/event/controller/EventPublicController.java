package ru.practicum.ewm.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventsSort;
import ru.practicum.ewm.event.service.EventPublicService;
import ru.practicum.ewm.stats.dto.Constants;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final EventPublicService eventService;

    @GetMapping
    Collection<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) Boolean paid,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
                                       LocalDateTime rangeStart,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
                                       LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                       @RequestParam(required = false) EventsSort sort,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                       @Positive @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return eventService.getEvents(ip, text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    EventFullDto getEvent(@PathVariable long eventId, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return eventService.getEvent(ip, eventId);
    }
}
