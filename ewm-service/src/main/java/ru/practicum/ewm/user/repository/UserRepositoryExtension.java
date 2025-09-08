package ru.practicum.ewm.user.repository;

import ru.practicum.ewm.user.model.User;

public interface UserRepositoryExtension {
    User findByIdOrThrow(long userId);
}
