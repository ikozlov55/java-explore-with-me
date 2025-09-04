package ru.practicum.ewm.category.repository;

import ru.practicum.ewm.category.model.Category;

public interface CategoryRepositoryExtension {
    Category findByIdOrThrow(long categoryId);
}
