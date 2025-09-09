package ru.practicum.ewm.testutils.builders;

import lombok.Builder;
import net.datafaker.Faker;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.spot.dto.UpdateSpotDto;

import java.util.Locale;

@Builder
public class UpdateSpot {
    private static final Faker faker = new Faker(Locale.of("RU"));
    @Builder.Default
    private String title = faker.lorem().characters(faker.random().nextInt(3, 50));
    @Builder.Default
    private String description = faker.lorem().characters(faker.random().nextInt(20, 1000));
    @Builder.Default
    private LocationDto location = new LocationDto(faker.random().nextDouble(55, 56),
            faker.random().nextDouble(37, 38));
    @Builder.Default
    private Integer radius = faker.random().nextInt(50, 500);

    public UpdateSpotDto dto() {
        return new UpdateSpotDto(title, description, location, radius);
    }
}
