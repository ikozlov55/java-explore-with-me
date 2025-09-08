package ru.practicum.ewm.event.dto;

import jakarta.validation.constraints.NotBlank;

public record LocationDto(@NotBlank Double lat, @NotBlank Double lon) {
}
