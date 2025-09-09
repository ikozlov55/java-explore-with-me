package ru.practicum.ewm.spot.dto;

import ru.practicum.ewm.event.dto.LocationDto;

public record SpotDto(Long id,
                      String title,
                      String description,
                      LocationDto location,
                      Integer radius) {
}

