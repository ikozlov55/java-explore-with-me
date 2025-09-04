package ru.practicum.ewm;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.practicum.ewm.testutils.EwmServiceApi;
import ru.practicum.ewm.testutils.TestData;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase
@Import({EwmServiceApi.class, TestData.class})
public class UserTest {
    @Autowired
    private EwmServiceApi serviceApi;
    @Autowired
    private TestData testData;
    private final Faker faker = new Faker(Locale.of("RU"));


    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto(null, faker.name().name(), faker.internet().emailAddress());

        serviceApi.createUser(userDto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()));
    }

    @Test
    void userNameIsRequired() throws Exception {
        UserDto userDto = new UserDto(null, null, faker.internet().emailAddress());

        serviceApi.createUser(userDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: name. Error: must not be blank")));
    }

    @Test
    void userEmailIsRequired() throws Exception {
        UserDto userDto = new UserDto(null, faker.name().name(), null);

        serviceApi.createUser(userDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: email. Error: must not be blank")));
    }

    @Test
    void userEmailMustBeValid() throws Exception {
        UserDto userDto = new UserDto(null, faker.name().name(), "@mail.ru");

        serviceApi.createUser(userDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(startsWith("Field: email. Error: must be a well-formed email address")));
    }

    @Test
    void userEmailMustBeUnique() throws Exception {
        UserDto userDto = testData.randomUser();

        serviceApi.createUser(userDto)
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUser() throws Exception {
        UserDto userDto = testData.randomUser();

        serviceApi.deleteUser(userDto.id())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserNotFound() throws Exception {
        long userId = 9999;
        String message = String.format("User with id=%d was not found", userId);

        serviceApi.deleteUser(userId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    void getUsers() throws Exception {
        int usersCount = 10;
        for (int i = 0; i < usersCount; i++) {
            testData.randomUser();
        }
        serviceApi.getUsers(List.of(), 0, usersCount)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(usersCount)));
    }

    @Test
    void getUsersByIds() throws Exception {
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            userIds.add(testData.randomUser().id());
        }
        for (int i = 0; i < 3; i++) {
            testData.randomUser();
        }
        serviceApi.getUsers(userIds, 0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userIds.size())))
                .andExpect(jsonPath("$.[*].id", contains(userIds.stream().map(Long::intValue).toArray())));
    }

    @Test
    void getUsersByIdsNotFound() throws Exception {
        List<Long> userIds = List.of(9999L);

        serviceApi.getUsers(userIds, 0, 10)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }
}
