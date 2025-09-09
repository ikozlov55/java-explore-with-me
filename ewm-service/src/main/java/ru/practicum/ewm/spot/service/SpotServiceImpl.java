package ru.practicum.ewm.spot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.ViewService;
import ru.practicum.ewm.spot.dto.*;
import ru.practicum.ewm.spot.model.Spot;
import ru.practicum.ewm.spot.repository.SpotRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {
    private final SpotRepository spotRepository;
    private final EventRepository eventRepository;
    private final ViewService viewService;

    @Override
    public SpotDto createSpot(NewSpotDto spotDto) {
        Spot spot = SpotMapper.toModel(spotDto);
        return SpotMapper.toDto(spotRepository.save(spot));
    }

    @Override
    public SpotDto updateSpot(long spotId, UpdateSpotDto spotDto) {
        Spot spot = spotRepository.findByIdOrThrow(spotId);
        if (spotDto.title() != null) {
            spot.setTitle(spotDto.title());
        }
        if (spotDto.description() != null) {
            spot.setDescription(spotDto.description());
        }
        if (spotDto.location() != null) {
            spot.setLat(spotDto.location().lat());
            spot.setLon(spotDto.location().lon());
        }
        if (spotDto.radius() != null) {
            spot.setRadius(spotDto.radius());
        }

        return SpotMapper.toDto(spotRepository.save(spot));
    }

    @Override
    public void deleteSpot(long spotId) {
        Spot spot = spotRepository.findByIdOrThrow(spotId);
        spotRepository.delete(spot);
    }

    @Override
    public Collection<SpotDto> getSpots(String text, int from, int size) {
        return spotRepository.getSpots(text, from, size).stream()
                .map(SpotMapper::toDto).toList();
    }

    @Override
    public SpotEventsDto getSpotEvents(long spotId) {
        Spot spot = spotRepository.findByIdOrThrow(spotId);
        List<EventShortDto> events = eventRepository.findEventsInRadius(spot.getLat(),
                        spot.getLon(),
                        spot.getRadius()
                ).stream().map(e -> {
                    long views = viewService.getEventViews(e.getId());
                    return EventMapper.toShortDto(e, views);
                })
                .toList();

        return new SpotEventsDto(
                spot.getId(),
                spot.getTitle(),
                spot.getDescription(),
                new LocationDto(spot.getLat(), spot.getLon()),
                spot.getRadius(),
                events
        );
    }
}
