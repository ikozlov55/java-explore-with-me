package ru.practicum.ewm.request.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.request.model.Request;

public class RequestRepositoryExtensionImpl implements RequestRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Request findByIdOrThrow(long requestId) {
        return entityManager.createQuery("SELECT r FROM Request r WHERE r.id = :requestId", Request.class)
                .setParameter("requestId", requestId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("Request with id=%d was not found", requestId);
                    return new NotFoundException(msg);
                });
    }
}
