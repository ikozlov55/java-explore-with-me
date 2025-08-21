package ru.practicum.ewm.stats.service.hit;

import ru.practicum.ewm.stats.dto.EndpointHitDto;

public final class EndpointHitMapper {
    private EndpointHitMapper() {
    }

    public static EndpointHit toModel(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null,
                endpointHitDto.app(),
                endpointHitDto.uri(),
                endpointHitDto.ip(),
                endpointHitDto.timestamp());
    }
}
