package ru.practicum.ewm.category.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.core.exception.NotFoundException;

public class CategoryRepositoryExtensionImpl implements CategoryRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Category findByIdOrThrow(long categoryId) {
        return entityManager.createQuery("SELECT c FROM Category c WHERE c.id = :categoryId", Category.class)
                .setParameter("categoryId", categoryId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("Category with id=%d was not found", categoryId);
                    return new NotFoundException(msg);
                });
    }
}
