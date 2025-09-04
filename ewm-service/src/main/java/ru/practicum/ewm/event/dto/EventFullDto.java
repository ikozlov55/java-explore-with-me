package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.stats.dto.Constants;
import ru.practicum.ewm.user.dto.UserDto;

import java.time.LocalDateTime;

public record EventFullDto(
        Long id,
        String title,
        String description,
        String annotation,
        EventState state,
        @JsonFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime eventDate,
        Boolean requestModeration,
        Integer participantLimit,
        Long confirmedRequests,
        Boolean paid,
        LocationDto location,
        CategoryDto category,
        UserDto initiator,
        Long views,
        @JsonFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime createdOn,
        @JsonFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime publishedOn
) {
}
