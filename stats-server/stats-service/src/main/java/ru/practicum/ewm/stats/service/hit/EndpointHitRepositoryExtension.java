package ru.practicum.ewm.stats.service.hit;

import ru.practicum.ewm.stats.dto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepositoryExtension {
    List<StatViewDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
