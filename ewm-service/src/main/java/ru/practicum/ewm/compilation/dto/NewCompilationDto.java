package ru.practicum.ewm.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewCompilationDto(@NotBlank @Size(min = 1, max = 50) String title,
                                Boolean pinned,
                                List<Long> events) {
}
