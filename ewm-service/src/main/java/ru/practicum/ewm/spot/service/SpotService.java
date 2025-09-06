package ru.practicum.ewm.spot.service;

import ru.practicum.ewm.spot.dto.NewSpotDto;
import ru.practicum.ewm.spot.dto.SpotDto;
import ru.practicum.ewm.spot.dto.SpotEventsDto;
import ru.practicum.ewm.spot.dto.UpdateSpotDto;

import java.util.Collection;

public interface SpotService {
    SpotDto createSpot(NewSpotDto spotDto);

    SpotDto updateSpot(long spotId, UpdateSpotDto spotDto);

    void deleteSpot(long spotId);

    Collection<SpotDto> getSpots(String text, int from, int size);

    SpotEventsDto getSpotEvents(long spotId);
}
