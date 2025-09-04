package ru.practicum.ewm.category.dto;

import ru.practicum.ewm.category.model.Category;

public final class CategoryMapper {
    private CategoryMapper() {
    }

    public static Category toModel(CategoryDto category) {
        return new Category(null, category.name());
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
