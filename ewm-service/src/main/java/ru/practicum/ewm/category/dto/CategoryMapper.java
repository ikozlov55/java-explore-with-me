package ru.practicum.ewm.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.model.Category;

@UtilityClass
public final class CategoryMapper {

    public static Category toModel(CategoryDto category) {
        return new Category(null, category.name());
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
