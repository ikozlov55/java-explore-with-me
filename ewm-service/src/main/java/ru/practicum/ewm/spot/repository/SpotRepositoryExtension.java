package ru.practicum.ewm.spot.repository;

import ru.practicum.ewm.spot.model.Spot;

import java.util.Collection;

public interface SpotRepositoryExtension {
    Spot findByIdOrThrow(long spotId);

    Collection<Spot> getSpots(String text, int from, int size);
}
