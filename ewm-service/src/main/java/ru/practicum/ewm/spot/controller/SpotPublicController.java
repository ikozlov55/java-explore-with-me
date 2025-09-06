package ru.practicum.ewm.spot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.spot.dto.SpotDto;
import ru.practicum.ewm.spot.dto.SpotEventsDto;
import ru.practicum.ewm.spot.service.SpotService;

import java.util.Collection;

@RestController
@RequestMapping("/spots")
@RequiredArgsConstructor
public class SpotPublicController {
    private final SpotService spotService;

    @GetMapping
    Collection<SpotDto> getSpots(@RequestParam(required = false) String text,
                                 @RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        return spotService.getSpots(text, from, size);
    }

    @GetMapping("/{spotId}/events")
    SpotEventsDto getSpotEvents(@PathVariable long spotId) {
        return spotService.getSpotEvents(spotId);
    }
}
