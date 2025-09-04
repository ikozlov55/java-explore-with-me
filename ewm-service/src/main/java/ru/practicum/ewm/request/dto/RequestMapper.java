package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.request.model.Request;

public final class RequestMapper {
    private RequestMapper() {
    }

    public static RequestDto toDto(Request request) {
        return new RequestDto(request.getId(), request.getEvent().getId(),
                request.getRequester().getId(), request.getStatus(), request.getCreatedAt());
    }
}
