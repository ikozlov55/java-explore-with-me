package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(long categoryId);

    CategoryDto updateCategory(long categoryId, CategoryDto categoryDto);

    CategoryDto getCategory(long categoryId);

    Collection<CategoryDto> getCategories(int from, int size);
}
