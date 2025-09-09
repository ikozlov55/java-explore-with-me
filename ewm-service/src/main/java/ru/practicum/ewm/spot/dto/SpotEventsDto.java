package ru.practicum.ewm.spot.dto;

import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.LocationDto;

import java.util.List;

public record SpotEventsDto(Long id,
                            String title,
                            String description,
                            LocationDto location,
                            Integer radius,
                            List<EventShortDto> events) {
}
