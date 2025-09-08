package ru.practicum.ewm.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.request.model.Request;

@UtilityClass
public final class RequestMapper {
    public static RequestDto toDto(Request request) {
        return new RequestDto(request.getId(), request.getEvent().getId(),
                request.getRequester().getId(), request.getStatus(), request.getCreatedAt());
    }
}
