package ru.practicum.ewm.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.user.model.User;

public class UserRepositoryExtensionImpl implements UserRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByIdOrThrow(long userId) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("User with id=%d was not found", userId);
                    return new NotFoundException(msg);
                });
    }
}
