package ru.practicum.ewm.request.repository;

import ru.practicum.ewm.request.model.Request;

public interface RequestRepositoryExtension {
    Request findByIdOrThrow(long requestId);
}
