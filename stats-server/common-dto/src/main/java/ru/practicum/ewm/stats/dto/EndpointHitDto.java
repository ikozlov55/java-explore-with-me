package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record EndpointHitDto(
        @NotBlank String app,
        @NotBlank String uri,
        @NotBlank String ip,
        @NotNull
        @PastOrPresent
        @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
        LocalDateTime timestamp
) {
}