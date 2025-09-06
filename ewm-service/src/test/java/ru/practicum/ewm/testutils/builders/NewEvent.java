package ru.practicum.ewm.testutils.builders;

import lombok.Builder;
import net.datafaker.Faker;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.util.Locale;

@Builder
public class NewEvent {
    private static final Faker faker = new Faker(Locale.of("RU"));

    @Builder.Default
    private String title = faker.lorem().characters(faker.random().nextInt(3, 120));
    @Builder.Default
    private String description = faker.lorem().characters(faker.random().nextInt(20, 7000));
    @Builder.Default
    private String annotation = faker.lorem().characters(faker.random().nextInt(20, 2000));
    @Builder.Default
    private LocalDateTime eventDate = LocalDateTime.now().plusHours(faker.random().nextInt(3, 48));
    @Builder.Default
    private Boolean requestModeration = faker.bool().bool();
    @Builder.Default
    private Integer participantLimit = faker.random().nextInt(0, 1000);
    @Builder.Default
    private Boolean paid = faker.bool().bool();
    @Builder.Default
    private LocationDto location = new LocationDto(faker.random().nextDouble(55, 56), faker.random().nextDouble(37, 38));
    private Long category;


    public NewEventDto dto() {
        return new NewEventDto(title, description, annotation, eventDate, requestModeration, participantLimit,
                paid, location, category);
    }
}
