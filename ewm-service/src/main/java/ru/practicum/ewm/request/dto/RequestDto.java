package ru.practicum.ewm.request.dto;

import java.time.LocalDateTime;

public record RequestDto(Long id, Long event, Long requester, RequestStatus status,
                         LocalDateTime created) {
}
