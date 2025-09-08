package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.core.exception.ConflictException;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.core.exception.ValidationException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.dto.*;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Collection<RequestDto> getUserEventRequests(long userId, long eventId) {
        return requestRepository.findByEventIdAndEventInitiatorId(eventId, userId).stream()
                .map(RequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public RequestStatusUpdateResult updateUserEventRequests(long userId, long eventId,
                                                             RequestStatusUpdateRequest statusUpdateRequest) {
        Event event = eventRepository.findByIdOrThrow(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Only published event requests can be updated");
        }
        Collection<Request> requests = requestRepository.findByEventIdAndEventInitiatorIdAndIdIn(eventId,
                userId, statusUpdateRequest.requestIds());
        if (requests.stream().anyMatch(r -> r.getStatus() != RequestStatus.PENDING)) {
            throw new ConflictException("Only pending requests can be updated");
        }
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        switch (statusUpdateRequest.status()) {
            case REJECTED -> {
                requests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
                rejectedRequests = requestRepository.saveAll(requests).stream()
                        .map(RequestMapper::toDto)
                        .toList();
            }
            case CONFIRMED -> {
                int limit = event.getParticipantLimit();
                long confirmed = event.getConfirmedRequests();
                if (limit > 0 && limit - confirmed < statusUpdateRequest.requestIds().size()) {
                    throw new ConflictException("Participant limit is not enough");
                }
                requests.forEach(r -> r.setStatus(RequestStatus.CONFIRMED));
                confirmedRequests = requestRepository.saveAll(requests).stream()
                        .map(RequestMapper::toDto)
                        .toList();
            }
            default -> throw new ValidationException("Field: status. Error: invalid state");
        }

        return new RequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    @Override
    public Collection<RequestDto> getRequests(long userId) {
        return requestRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .toList();
    }

    @Override
    public RequestDto createRequest(long userId, long eventId) {
        User user = userRepository.findByIdOrThrow(userId);
        Event event = eventRepository.findByIdOrThrow(eventId);
        String error = null;

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            error = "Unable create request: request for this event already exists";
        }
        if (event.getInitiator().getId().equals(userId)) {
            error = "Unable create request: no request needed for initiator";
        }
        if (event.getState() != EventState.PUBLISHED) {
            error = "Unable create request: event unpublished";
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            error = "Unable create request: participant limit exceeded";
        }

        if (error != null) {
            throw new ConflictException(error);
        }
        RequestStatus status = !event.getRequestModeration() || event.getParticipantLimit() == 0
                ? RequestStatus.CONFIRMED
                : RequestStatus.PENDING;
        Request request = requestRepository.save(new Request(null, event, user,
                status,
                null));

        return RequestMapper.toDto(request);
    }

    @Override
    public RequestDto cancelRequest(long userId, long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId);
        if (request == null) {
            String msg = String.format("Request with id=%d was not found", requestId);
            throw new NotFoundException(msg);
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            String msg = String.format("Unable to cancel request: request is %s", request.getStatus());
            throw new ConflictException(msg);
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }
}
