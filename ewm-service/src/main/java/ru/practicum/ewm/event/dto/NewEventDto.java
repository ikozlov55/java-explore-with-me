package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.core.validation.EventDate;
import ru.practicum.ewm.stats.dto.Constants;

import java.time.LocalDateTime;

public record NewEventDto(
        @NotBlank
        @Length(min = 3, max = 120)
        String title,
        @NotBlank
        @Length(min = 20, max = 7000)
        String description,
        @NotBlank
        @Length(min = 20, max = 2000)
        String annotation,
        @NotNull
        @EventDate
        @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
        LocalDateTime eventDate,
        Boolean requestModeration,
        @PositiveOrZero
        Integer participantLimit,
        Boolean paid,
        @NotNull
        LocationDto location,
        @NotNull
        Long category
) {
}

