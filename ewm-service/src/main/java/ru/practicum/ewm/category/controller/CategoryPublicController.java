package ru.practicum.ewm.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    CategoryDto getCategory(@PathVariable long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @GetMapping
    Collection<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                          @Positive @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }
}
