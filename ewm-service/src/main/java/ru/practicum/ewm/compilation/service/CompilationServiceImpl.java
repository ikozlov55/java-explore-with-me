package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.core.exception.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.ViewService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ViewService viewService;

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return compilationRepository.findByPinnedIn(pinned == null ? List.of(true, false) : List.of(pinned), page)
                .stream().map(c -> CompilationMapper.toDto(c, viewService))
                .toList();
    }

    @Override
    public CompilationDto getCompilation(long compilationId) {
        Compilation compilation = compilationRepository.findByIdOrThrow(compilationId);
        return CompilationMapper.toDto(compilation, viewService);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        List<Event> events = compilationDto.events() != null
                ? eventRepository.findByIdIn(compilationDto.events())
                : List.of();
        Compilation compilation = CompilationMapper.toModel(compilationDto, events);
        return CompilationMapper.toDto(compilationRepository.save(compilation), viewService);
    }

    @Override
    public void deleteCompilation(long compilationId) {
        Compilation compilation = compilationRepository.findByIdOrThrow(compilationId);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(long compilationId, UpdateCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.findByIdOrThrow(compilationId);
        if (compilationDto.pinned() != null) {
            compilation.setPinned(compilationDto.pinned());
        }
        if (compilationDto.title() != null) {
            compilation.setTitle(compilationDto.title());
        }
        if (compilationDto.events() != null) {
            List<Event> events = eventRepository.findByIdIn(compilationDto.events());
            Set<Long> diff = new HashSet<>(compilationDto.events());
            diff.removeAll(events.stream().map(Event::getId).collect(Collectors.toSet()));
            if (!diff.isEmpty()) {
                String msg = String.format("Events with ids: %s not found", diff);
                throw new NotFoundException(msg);
            }
            compilation.setEvents(events);
        }

        return CompilationMapper.toDto(compilationRepository.save(compilation), viewService);
    }
}
