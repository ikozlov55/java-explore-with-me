package ru.practicum.ewm.testutils;

import net.datafaker.Faker;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.dto.NewEventDto;

import java.time.LocalDateTime;
import java.util.Locale;

public class NewEventDtoBuilder {
    private String title;
    private String description;
    private String annotation;
    private LocalDateTime eventDate;
    private Boolean requestModeration;
    private Integer participantLimit;
    private Boolean paid;
    private LocationDto location;
    private Long category;

    public NewEventDtoBuilder(Long category) {
        Faker faker = new Faker(Locale.of("RU"));
        this.title = faker.lorem().characters(faker.random().nextInt(3, 120));
        this.description = faker.lorem().characters(faker.random().nextInt(20, 7000));
        this.annotation = faker.lorem().characters(faker.random().nextInt(20, 2000));
        this.eventDate = LocalDateTime.now().plusHours(faker.random().nextInt(3, 48));
        this.requestModeration = faker.bool().bool();
        this.participantLimit = faker.random().nextInt(0, 1000);
        this.paid = faker.bool().bool();
        this.location = new LocationDto(faker.random().nextDouble(55, 56), faker.random().nextDouble(37, 38));
        this.category = category;
    }

    public NewEventDtoBuilder title(String title) {
        this.title = title;
        return this;
    }

    public NewEventDtoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public NewEventDtoBuilder annotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    public NewEventDtoBuilder eventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public NewEventDtoBuilder requestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
        return this;
    }

    public NewEventDtoBuilder participantLimit(Integer participantLimit) {
        this.participantLimit = participantLimit;
        return this;
    }

    public NewEventDtoBuilder paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public NewEventDtoBuilder location(LocationDto location) {
        this.location = location;
        return this;
    }

    public NewEventDto build() {
        return new NewEventDto(title, description, annotation, eventDate, requestModeration, participantLimit,
                paid, location, category);
    }
}
