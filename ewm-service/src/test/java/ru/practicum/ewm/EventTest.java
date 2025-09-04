package ru.practicum.ewm;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventStateAction;
import ru.practicum.ewm.stats.dto.Constants;
import ru.practicum.ewm.testutils.EwmServiceApi;
import ru.practicum.ewm.testutils.NewEventDtoBuilder;
import ru.practicum.ewm.testutils.TestConfig;
import ru.practicum.ewm.testutils.TestData;
import ru.practicum.ewm.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@Import({EwmServiceApi.class, TestData.class, TestConfig.class})
public class EventTest {
    @Autowired
    private EwmServiceApi serviceApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void getUserEvents() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        String eventDate = event.eventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));

        serviceApi.getUserEvents(user.id(), 0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(event.id()))
                .andExpect(jsonPath("$.[0].title").value(event.title()))
                .andExpect(jsonPath("$.[0].annotation").value(event.annotation()))
                .andExpect(jsonPath("$.[0].category.id").value(category.id()))
                .andExpect(jsonPath("$.[0].category.name").value(category.name()))
                .andExpect(jsonPath("$.[0].confirmedRequests").value(0))
                .andExpect(jsonPath("$.[0].eventDate").value(eventDate))
                .andExpect(jsonPath("$.[0].initiator.id").value(user.id()))
                .andExpect(jsonPath("$.[0].initiator.name").value(user.name()))
                .andExpect(jsonPath("$.[0].paid").value(event.paid()))
                .andExpect(jsonPath("$.[0].views").value(0));

    }

    @Test
    void getUserEventsUserNotFound() throws Exception {
        long userId = 9999;
        String message = String.format("User with id=%d was not found", userId);

        serviceApi.getUserEvents(userId, 0, 10)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void getUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        String eventDate = event.eventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));

        serviceApi.getUserEvent(user.id(), event.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(event.id()))
                .andExpect(jsonPath("$.title").value(event.title()))
                .andExpect(jsonPath("$.annotation").value(event.annotation()))
                .andExpect(jsonPath("$.category.id").value(category.id()))
                .andExpect(jsonPath("$.category.name").value(category.name()))
                .andExpect(jsonPath("$.confirmedRequests").value(0))
                .andExpect(jsonPath("$.eventDate").value(eventDate))
                .andExpect(jsonPath("$.initiator.id").value(user.id()))
                .andExpect(jsonPath("$.initiator.name").value(user.name()))
                .andExpect(jsonPath("$.paid").value(event.paid()))
                .andExpect(jsonPath("$.views").value(0));
    }

    @Test
    void getUserEventNotFound() throws Exception {
        UserDto user = testData.randomUser();
        long eventId = 9999;
        String message = String.format("Event with id=%d was not found", eventId);

        serviceApi.getUserEvent(user.id(), eventId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void createUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDto(
                faker.lorem().characters(faker.random().nextInt(3, 120)),
                faker.lorem().characters(faker.random().nextInt(20, 7000)),
                faker.lorem().characters(faker.random().nextInt(20, 2000)),
                LocalDateTime.now().plusHours(faker.random().nextInt(3, 48)),
                faker.bool().bool(),
                faker.random().nextInt(0, 1000),
                faker.bool().bool(),
                new LocationDto(faker.random().nextDouble(55, 56), faker.random().nextDouble(37, 38)),
                category.id()
        );
        String eventDate = eventDto.eventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.title").value(eventDto.title()))
                .andExpect(jsonPath("$.description").value(eventDto.description()))
                .andExpect(jsonPath("$.annotation").value(eventDto.annotation()))
                .andExpect(jsonPath("$.state").value(EventState.PENDING.name()))
                .andExpect(jsonPath("$.eventDate").value(eventDate))
                .andExpect(jsonPath("$.requestModeration").value(eventDto.requestModeration()))
                .andExpect(jsonPath("$.participantLimit").value(eventDto.participantLimit()))
                .andExpect(jsonPath("$.confirmedRequests").value(0))
                .andExpect(jsonPath("$.paid").value(eventDto.paid()))
                .andExpect(jsonPath("$.location.lat").value(eventDto.location().lat()))
                .andExpect(jsonPath("$.location.lon").value(eventDto.location().lon()))
                .andExpect(jsonPath("$.category.id").value(category.id()))
                .andExpect(jsonPath("$.category.name").value(category.name()))
                .andExpect(jsonPath("$.initiator.id").value(user.id()))
                .andExpect(jsonPath("$.initiator.name").value(user.name()))
                .andExpect(jsonPath("$.views").value(0))
                .andExpect(jsonPath("$.createdOn").value(not(emptyString())))
                .andExpect(jsonPath("$.publishedOn").value(nullValue()));
    }

    @Test
    void userNotFoundOnCreateUserEvent() throws Exception {
        long userId = 9999;
        String message = String.format("User with id=%d was not found", userId);
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id()).build();

        serviceApi.createUserEvent(userId, eventDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void categoryNotFoundOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        long categoryId = 9999;
        String message = String.format("Category with id=%d was not found", categoryId);
        CategoryDto category = new CategoryDto(categoryId, null);
        NewEventDto eventDto = new NewEventDtoBuilder(category.id()).build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void annotationIsRequiredOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .annotation(null)
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: annotation. Error: must not be blank")));
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 2001})
    void annotationHasLimitsOnCreateUserEvent(int length) throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .annotation(faker.lorem().characters(length))
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: annotation. Error: length must be between 20 and 2000")));
    }

    @Test
    void categoryIsRequiredOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        NewEventDto eventDto = new NewEventDtoBuilder(null).build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: category. Error: must not be null")));

    }

    @Test
    void descriptionIsRequiredOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .description(null)
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: description. Error: must not be blank")));
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 7001})
    void descriptionHasLimitsOnCreateUserEvent(int length) throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .description(faker.lorem().characters(length))
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: description. Error: length must be between 20 and 7000")));
    }

    @Test
    void eventDateIsRequiredOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .eventDate(null)
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: eventDate. Error: must not be null. Value: null")));
    }

    @Test
    void eventDateMustBe2HoursInFutureOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .eventDate(LocalDateTime.now().plusMinutes(119))
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: eventDate. Error: date must be at least two hours in future")));
    }

    @Test
    void locationIsRequiredOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .location(null)
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: location. Error: must not be null")));
    }

    @Test
    void titleIsRequiredOnCreateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .title(null)
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: title. Error: must not be blank")));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 121})
    void titleHasLimitsOnCreateUserEvent(int length) throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        NewEventDto eventDto = new NewEventDtoBuilder(category.id())
                .title(faker.lorem().characters(length))
                .build();

        serviceApi.createUserEvent(user.id(), eventDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: title. Error: length must be between 3 and 120")));
    }

    @Test
    void updateUserEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        CategoryDto newCategory = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        NewEventDto updateData = new NewEventDtoBuilder(newCategory.id()).build();
        UpdateEventDto updateEventDto = new UpdateEventDto(
                updateData.title(),
                updateData.description(),
                updateData.annotation(),
                updateData.eventDate(),
                updateData.requestModeration(),
                updateData.participantLimit(),
                updateData.paid(),
                updateData.location(),
                updateData.category(),
                EventStateAction.CANCEL_REVIEW
        );
        String eventDate = updateEventDto.eventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));

        serviceApi.updateUserEvent(user.id(), event.id(), updateEventDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.title").value(updateEventDto.title()))
                .andExpect(jsonPath("$.description").value(updateEventDto.description()))
                .andExpect(jsonPath("$.state").value(EventState.CANCELED.name()))
                .andExpect(jsonPath("$.annotation").value(updateEventDto.annotation()))
                .andExpect(jsonPath("$.eventDate").value(eventDate))
                .andExpect(jsonPath("$.requestModeration").value(updateEventDto.requestModeration()))
                .andExpect(jsonPath("$.participantLimit").value(updateEventDto.participantLimit()))
                .andExpect(jsonPath("$.confirmedRequests").value(0))
                .andExpect(jsonPath("$.paid").value(updateEventDto.paid()))
                .andExpect(jsonPath("$.location.lat").value(updateEventDto.location().lat()))
                .andExpect(jsonPath("$.location.lon").value(updateEventDto.location().lon()))
                .andExpect(jsonPath("$.category.id").value(newCategory.id()))
                .andExpect(jsonPath("$.category.name").value(newCategory.name()))
                .andExpect(jsonPath("$.initiator.id").value(user.id()))
                .andExpect(jsonPath("$.initiator.name").value(user.name()))
                .andExpect(jsonPath("$.views").value(0))
                .andExpect(jsonPath("$.createdOn").value(not(emptyString())))
                .andExpect(jsonPath("$.publishedOn").value(nullValue()));
    }

    @Test
    void getAdminEvents() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);

        serviceApi.getAdminEvents(List.of(user.id()),
                        List.of(event.state()),
                        List.of(category.id()),
                        event.eventDate().minusMinutes(10),
                        event.eventDate().plusMinutes(10),
                        0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(event.id()));
    }

    @Test
    void getAdminEventsEmpty() throws Exception {
        serviceApi.getAdminEvents(List.of(9999L), null, null, null,
                        null, 0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    void updateAdminEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        CategoryDto newCategory = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        NewEventDto updateData = new NewEventDtoBuilder(newCategory.id()).build();
        UpdateEventDto updateEventDto = new UpdateEventDto(
                updateData.title(),
                updateData.description(),
                updateData.annotation(),
                updateData.eventDate(),
                updateData.requestModeration(),
                updateData.participantLimit(),
                updateData.paid(),
                updateData.location(),
                updateData.category(),
                EventStateAction.PUBLISH_EVENT
        );
        String eventDate = updateEventDto.eventDate().format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT));

        serviceApi.updateAdminEvent(event.id(), updateEventDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.title").value(updateEventDto.title()))
                .andExpect(jsonPath("$.description").value(updateEventDto.description()))
                .andExpect(jsonPath("$.state").value(EventState.PUBLISHED.name()))
                .andExpect(jsonPath("$.annotation").value(updateEventDto.annotation()))
                .andExpect(jsonPath("$.eventDate").value(eventDate))
                .andExpect(jsonPath("$.requestModeration").value(updateEventDto.requestModeration()))
                .andExpect(jsonPath("$.participantLimit").value(updateEventDto.participantLimit()))
                .andExpect(jsonPath("$.confirmedRequests").value(0))
                .andExpect(jsonPath("$.paid").value(updateEventDto.paid()))
                .andExpect(jsonPath("$.location.lat").value(updateEventDto.location().lat()))
                .andExpect(jsonPath("$.location.lon").value(updateEventDto.location().lon()))
                .andExpect(jsonPath("$.category.id").value(newCategory.id()))
                .andExpect(jsonPath("$.category.name").value(newCategory.name()))
                .andExpect(jsonPath("$.initiator.id").value(user.id()))
                .andExpect(jsonPath("$.initiator.name").value(user.name()))
                .andExpect(jsonPath("$.views").value(0))
                .andExpect(jsonPath("$.createdOn").value(not(emptyString())))
                .andExpect(jsonPath("$.publishedOn").value(not(emptyString())));
    }

    @Test
    void getPublicEvent() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        testData.publishEvent(event.id());

        serviceApi.getPublicEvent(event.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(event.id()));
    }

    @Test
    void getPublicEventNotFound() throws Exception {
        serviceApi.getPublicEvent(99999L)
                .andExpect(status().isNotFound());
    }

    @Test
    void getPublicEventNotFoundIfNotPublished() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);

        serviceApi.getPublicEvent(event.id())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPublicEventViewCountIncrease() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        testData.publishEvent(event.id());
        int viewCount = 1;

        for (int i = 0; i < 5; i++) {
            serviceApi.getPublicEvent(event.id())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.views").value(viewCount));
            viewCount++;
        }
    }

    @Test
    void getPublicEvents() throws Exception {
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event = testData.randomEvent(user, category);
        testData.publishEvent(event.id());

        serviceApi.getPublicEvents(
                        event.annotation().substring(0, event.annotation().length() / 2),
                        List.of(category.id()),
                        event.paid(),
                        event.eventDate().minusMinutes(10),
                        event.eventDate().plusMinutes(10),
                        true,
                        null,
                        0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(event.id()));
    }
}
