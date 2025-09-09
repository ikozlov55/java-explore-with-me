package ru.practicum.ewm.spot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.dto.LocationDto;


public record NewSpotDto(@NotBlank @Length(min = 3, max = 50) String title,
                         @NotBlank @Length(min = 20, max = 7000) String description,
                         @NotNull LocationDto location,
                         @NotNull @Positive Integer radius) {
}