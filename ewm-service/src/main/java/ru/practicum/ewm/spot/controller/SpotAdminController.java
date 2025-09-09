package ru.practicum.ewm.spot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.spot.dto.NewSpotDto;
import ru.practicum.ewm.spot.dto.SpotDto;
import ru.practicum.ewm.spot.dto.UpdateSpotDto;
import ru.practicum.ewm.spot.service.SpotService;

@RestController
@RequestMapping("/admin/spots")
@RequiredArgsConstructor
public class SpotAdminController {
    private final SpotService spotService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SpotDto createSpot(@Valid @RequestBody NewSpotDto spotDto) {
        return spotService.createSpot(spotDto);
    }

    @PatchMapping("/{spotId}")
    SpotDto updateSpot(@PathVariable long spotId, @Valid @RequestBody UpdateSpotDto spotDto) {
        return spotService.updateSpot(spotId, spotDto);
    }

    @DeleteMapping("/{spotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSpot(@PathVariable long spotId) {
        spotService.deleteSpot(spotId);
    }
}
