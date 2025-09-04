package ru.practicum.ewm.compilation.repository;

import ru.practicum.ewm.compilation.model.Compilation;

public interface CompilationRepositoryExtension {
    Compilation findByIdOrThrow(long compilationId);
}
