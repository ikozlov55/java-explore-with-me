package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;

import java.util.Collection;

public interface RequestService {
    Collection<RequestDto> getUserEventRequests(long userId, long eventId);

    RequestStatusUpdateResult updateUserEventRequests(long userId, long eventId,
                                                      RequestStatusUpdateRequest statusUpdateRequest);

    Collection<RequestDto> getRequests(long userId);

    RequestDto createRequest(long userId, long eventId);

    RequestDto cancelRequest(long userId, long requestId);
}
