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
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.LocationDto;
import ru.practicum.ewm.spot.dto.NewSpotDto;
import ru.practicum.ewm.spot.dto.SpotDto;
import ru.practicum.ewm.spot.dto.UpdateSpotDto;
import ru.practicum.ewm.testutils.EwmServiceApi;
import ru.practicum.ewm.testutils.TestConfig;
import ru.practicum.ewm.testutils.TestData;
import ru.practicum.ewm.testutils.builders.NewSpot;
import ru.practicum.ewm.testutils.builders.UpdateSpot;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@Sql({"/init.sql"})
@Import({EwmServiceApi.class, TestData.class, TestConfig.class})
public class SpotTest {
    @Autowired
    private EwmServiceApi serviceApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void createSpot() throws Exception {
        NewSpotDto spot = new NewSpotDto(
                faker.lorem().characters(faker.random().nextInt(3, 50)),
                faker.lorem().characters(faker.random().nextInt(20, 1000)),
                new LocationDto(faker.random().nextDouble(55, 56), faker.random().nextDouble(37, 38)),
                faker.random().nextInt(50, 500)
        );

        serviceApi.createSpot(spot)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.title").value(spot.title()))
                .andExpect(jsonPath("$.description").value(spot.description()))
                .andExpect(jsonPath("$.location.lat").value(spot.location().lat()))
                .andExpect(jsonPath("$.location.lon").value(spot.location().lon()))
                .andExpect(jsonPath("$.radius").value(spot.radius()));
    }

    @Test
    void titleIsRequiredOnCreateSpot() throws Exception {
        NewSpotDto spot = NewSpot.builder().title(null).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: title. Error: must not be blank")));
    }

    @Test
    void titleMustBeUniqueOnCreateSpot() throws Exception {
        SpotDto spot = testData.randomSpot();
        NewSpotDto newSpot = NewSpot.builder().title(spot.title()).build().dto();

        serviceApi.createSpot(newSpot)
                .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 51})
    void titleLimitsOnCreateSpot(int length) throws Exception {
        NewSpotDto spot = NewSpot.builder().title(faker.lorem().characters(length)).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: title. Error: length must be between 3 and 50")));
    }

    @Test
    void descriptionIsRequiredOnCreateSpot() throws Exception {
        NewSpotDto spot = NewSpot.builder().description(null).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: description. Error: must not be blank")));
    }

    @ParameterizedTest
    @ValueSource(ints = {19, 7001})
    void descriptionLimitsOnCreateSpot(int length) throws Exception {
        NewSpotDto spot = NewSpot.builder().description(faker.lorem().characters(length)).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: description. Error: length must be between 20 and 7000")));
    }

    @Test
    void locationIsRequiredOnCreateSpot() throws Exception {
        NewSpotDto spot = NewSpot.builder().location(null).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: location. Error: must not be null")));
    }

    @Test
    void radiusIsRequiredOnCreateSpot() throws Exception {
        NewSpotDto spot = NewSpot.builder().radius(null).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: radius. Error: must not be null")));
    }

    @Test
    void radiusMustBePositiveOnCreateSpot() throws Exception {
        NewSpotDto spot = NewSpot.builder().radius(-1).build().dto();

        serviceApi.createSpot(spot)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: radius. Error: must be greater than 0")));
    }

    @Test
    void updateSpot() throws Exception {
        SpotDto spot = testData.randomSpot();
        UpdateSpotDto spotDto = UpdateSpot.builder().build().dto();
        serviceApi.updateSpot(spot.id(), spotDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(spot.id()))
                .andExpect(jsonPath("$.title").value(spotDto.title()))
                .andExpect(jsonPath("$.description").value(spotDto.description()))
                .andExpect(jsonPath("$.location.lat").value(spotDto.location().lat()))
                .andExpect(jsonPath("$.location.lon").value(spotDto.location().lon()))
                .andExpect(jsonPath("$.radius").value(spotDto.radius()));
    }

    @Test
    void spotNotFoundOnUpdateSpot() throws Exception {
        long spotId = 9999;
        String message = String.format("Spot with id=%d was not found", spotId);
        UpdateSpotDto spotDto = UpdateSpot.builder().build().dto();

        serviceApi.updateSpot(spotId, spotDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void deleteSpot() throws Exception {
        SpotDto spot = testData.randomSpot();

        serviceApi.deleteSpot(spot.id())
                .andExpect(status().isNoContent());
    }

    @Test
    void spotNotFoundOnDeleteSpot() throws Exception {
        long spotId = 9999;
        String message = String.format("Spot with id=%d was not found", spotId);

        serviceApi.deleteSpot(spotId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void getSpots() throws Exception {
        for (int i = 0; i < 10; i++) {
            testData.randomSpot();
        }

        serviceApi.getSpots(null, 0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void getSpotsSearchByText() throws Exception {
        SpotDto spot = testData.randomSpot();
        for (int i = 0; i < 10; i++) {
            testData.randomSpot();
        }

        serviceApi.getSpots(spot.title(), 0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(spot.id()))
                .andExpect(jsonPath("$.[0].title").value(spot.title()))
                .andExpect(jsonPath("$.[0].description").value(spot.description()))
                .andExpect(jsonPath("$.[0].location.lat").value(spot.location().lat()))
                .andExpect(jsonPath("$.[0].location.lon").value(spot.location().lon()))
                .andExpect(jsonPath("$.[0].radius").value(spot.radius()));
    }

    @Test
    void getSpotEventsWithEventsInRadius() throws Exception {
        SpotDto spot1 = testData.randomSpot(TestData.MSK_CENTER, 500);
        SpotDto spot2 = testData.randomSpot(TestData.MSK_CENTER, 200);
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event1 = testData.randomEvent(user, category, TestData.MSK_CENTER_PLUS_100);
        testData.publishEvent(event1.id());
        EventFullDto event2 = testData.randomEvent(user, category, TestData.MSK_CENTER_PLUS_300);
        testData.publishEvent(event2.id());
        int event1Id = event1.id().intValue();
        int event2Id = event2.id().intValue();

        serviceApi.getSpotEvents(spot1.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events").value(not(empty())))
                .andExpect(jsonPath("$.events[*].id", hasItems(event1Id, event2Id)));

        serviceApi.getSpotEvents(spot2.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events").value(not(empty())))
                .andExpect(jsonPath("$.events[*].id", hasItems(event1Id)))
                .andExpect(jsonPath("$.events[*].id", not(hasItems(event2Id))));
    }

    @Test
    void getSpotEventsWithoutEventsInRadius() throws Exception {
        SpotDto spot = testData.randomSpot(new LocationDto(55.0, 37.0), 1000);
        for (int i = 0; i < 5; i++) {
            testData.randomSpot();
        }

        serviceApi.getSpotEvents(spot.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(spot.id()))
                .andExpect(jsonPath("$.title").value(spot.title()))
                .andExpect(jsonPath("$.description").value(spot.description()))
                .andExpect(jsonPath("$.location.lat").value(spot.location().lat()))
                .andExpect(jsonPath("$.location.lon").value(spot.location().lon()))
                .andExpect(jsonPath("$.radius").value(spot.radius()))
                .andExpect(jsonPath("$.events").value(empty()));
    }

    @Test
    void onlyPublishedEventsAppearInGetSpotEvents() throws Exception {
        SpotDto spot = testData.randomSpot(TestData.MSK_CENTER, 500);
        UserDto user = testData.randomUser();
        CategoryDto category = testData.randomCategory();
        EventFullDto event1 = testData.randomEvent(user, category, TestData.MSK_CENTER_PLUS_100);
        EventFullDto event2 = testData.randomEvent(user, category, TestData.MSK_CENTER_PLUS_300);
        testData.publishEvent(event2.id());
        int event1Id = event1.id().intValue();
        int event2Id = event2.id().intValue();


        serviceApi.getSpotEvents(spot.id())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events").value(not(empty())))
                .andExpect(jsonPath("$.events[*].id", hasItem(event2Id)))
                .andExpect(jsonPath("$.events[*].id", not(hasItem(event1Id))));
    }

    @Test
    void spotNotFoundOnGetSpotEvents() throws Exception {
        long spotId = 9999;
        String message = String.format("Spot with id=%d was not found", spotId);

        serviceApi.getSpotEvents(spotId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

}
