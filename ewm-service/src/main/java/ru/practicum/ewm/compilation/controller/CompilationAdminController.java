package ru.practicum.ewm.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}")
    CompilationDto updateCompilation(@PathVariable long compilationId,
                                     @Valid @RequestBody UpdateCompilationDto compilationDto) {
        return compilationService.updateCompilation(compilationId, compilationDto);
    }
}
