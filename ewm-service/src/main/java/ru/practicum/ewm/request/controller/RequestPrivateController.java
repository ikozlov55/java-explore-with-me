package ru.practicum.ewm.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.RequestStatusUpdateResult;
import ru.practicum.ewm.request.service.RequestService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    Collection<RequestDto> getUserEventRequests(@PathVariable long userId, @PathVariable long eventId) {
        return requestService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    RequestStatusUpdateResult updateUserEventRequests(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody RequestStatusUpdateRequest statusUpdateRequest) {
        return requestService.updateUserEventRequests(userId, eventId, statusUpdateRequest);
    }

    @GetMapping("/users/{userId}/requests")
    Collection<RequestDto> getRequests(@PathVariable long userId) {
        return requestService.getRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    RequestDto createRequest(@PathVariable long userId, @RequestParam long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    RequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
