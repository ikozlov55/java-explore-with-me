package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(long compilationId);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(long compilationId);

    CompilationDto updateCompilation(long compilationId, UpdateCompilationDto compilationDto);
}
