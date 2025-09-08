package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.stats.dto.Constants;
import ru.practicum.ewm.user.dto.UserDto;

import java.time.LocalDateTime;

public record EventShortDto(
        Long id,
        String title,
        String annotation,
        @JsonFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime eventDate,
        Long confirmedRequests,
        Boolean paid,
        CategoryDto category,
        UserDto initiator,
        Long views
) {
}
