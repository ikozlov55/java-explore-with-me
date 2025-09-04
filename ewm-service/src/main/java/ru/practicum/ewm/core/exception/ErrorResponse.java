package ru.practicum.ewm.core.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.stats.dto.Constants;

import java.time.LocalDateTime;

public record ErrorResponse(String status, String reason, String message,
                            @JsonFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime timestamp) {
}
