package ru.practicum.ewm.stats.service.hit;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.StatViewDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    void saveEndpointHit(EndpointHitDto endpointHitDto);

    List<StatViewDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
