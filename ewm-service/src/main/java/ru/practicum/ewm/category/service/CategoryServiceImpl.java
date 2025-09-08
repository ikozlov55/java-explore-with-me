package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toModel(categoryDto));
        return CategoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findByIdOrThrow(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto updateCategory(long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findByIdOrThrow(categoryId);
        category.setName(categoryDto.name());
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(updatedCategory);
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        Category category = categoryRepository.findByIdOrThrow(categoryId);
        return CategoryMapper.toDto(category);
    }

    @Override
    public Collection<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryRepository.findAll(page).stream().map(CategoryMapper::toDto).toList();
    }
}
