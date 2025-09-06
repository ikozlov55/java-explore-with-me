package ru.practicum.ewm.spot.dto;

import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.dto.LocationDto;


public record UpdateSpotDto(@Length(min = 3, max = 50) String title,
                            @Length(min = 20, max = 7000) String description,
                            LocationDto location,
                            @Positive Integer radius) {
}