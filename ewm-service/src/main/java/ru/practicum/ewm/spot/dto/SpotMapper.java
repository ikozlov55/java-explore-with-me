package ru.practicum.ewm.spot.dto;

import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.spot.model.Spot;

public final class SpotMapper {
    private SpotMapper() {

    }

    public static Spot toModel(NewSpotDto spotDto) {
        return new Spot(null,
                spotDto.title(),
                spotDto.description(),
                spotDto.location().lat(),
                spotDto.location().lon(),
                spotDto.radius());
    }

    public static SpotDto toDto(Spot spot) {
        return new SpotDto(spot.getId(),
                spot.getTitle(),
                spot.getDescription(),
                new LocationDto(spot.getLat(), spot.getLon()),
                spot.getRadius());
    }

}
