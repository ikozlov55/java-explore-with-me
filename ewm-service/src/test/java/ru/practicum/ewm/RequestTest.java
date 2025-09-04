package ru.practicum.ewm;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.testutils.EwmServiceApi;
import ru.practicum.ewm.testutils.TestConfig;
import ru.practicum.ewm.testutils.TestData;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@Import({EwmServiceApi.class, TestData.class, TestConfig.class})
public class RequestTest {
    @Autowired
    private EwmServiceApi serviceApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));

    @Test
    void getUserEventRequests() throws Exception {
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
