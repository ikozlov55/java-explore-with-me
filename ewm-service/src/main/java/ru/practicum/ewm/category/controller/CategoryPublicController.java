package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    CategoryDto getCategory(@PathVariable long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @GetMapping
    Collection<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }
}
