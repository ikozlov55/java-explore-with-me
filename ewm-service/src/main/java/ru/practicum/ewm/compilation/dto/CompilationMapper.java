package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.ViewService;

import java.util.List;

public final class CompilationMapper {
    private CompilationMapper() {
    }

    public static Compilation toModel(NewCompilationDto compilationDto, List<Event> events) {
        return new Compilation(null,
                compilationDto.title(),
                compilationDto.pinned() != null ? compilationDto.pinned() : false,
                events);
    }

    public static CompilationDto toDto(Compilation compilation, ViewService viewService) {

        return new CompilationDto(compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                compilation.getEvents().stream().map(event -> {
                    long views = viewService.getEventViews(event.getId());
                    return EventMapper.toShortDto(event, views);
                }).toList());
    }
}
