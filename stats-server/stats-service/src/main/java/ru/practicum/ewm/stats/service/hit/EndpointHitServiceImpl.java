package ru.practicum.ewm.stats.service.hit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.StatViewDto;
import ru.practicum.ewm.stats.service.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void saveEndpointHit(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(EndpointHitMapper.toModel(endpointHitDto));
    }

    @Override
    public List<StatViewDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end) || start.equals(end)) {
            throw new ValidationException("Invalid date interval!");
        }
        return endpointHitRepository.getStats(start, end, uris, unique);
    }
}
