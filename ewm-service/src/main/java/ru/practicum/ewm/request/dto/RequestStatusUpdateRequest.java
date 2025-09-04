package ru.practicum.ewm.request.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RequestStatusUpdateRequest(@NotEmpty List<Long> requestIds, @NotNull RequestStatus status) {
}
