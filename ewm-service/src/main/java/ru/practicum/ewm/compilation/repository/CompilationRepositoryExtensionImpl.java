package ru.practicum.ewm.compilation.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.core.exception.NotFoundException;

public class CompilationRepositoryExtensionImpl implements CompilationRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Compilation findByIdOrThrow(long compilationId) {
        return entityManager.createQuery("SELECT c FROM Compilation c WHERE c.id = :compilationId",
                        Compilation.class)
                .setParameter("compilationId", compilationId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("Compilation with id=%d was not found", compilationId);
                    return new NotFoundException(msg);
                });
    }
}
