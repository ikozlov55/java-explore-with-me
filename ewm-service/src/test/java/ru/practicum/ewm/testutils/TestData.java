package ru.practicum.ewm.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.EventStateAction;
import ru.practicum.ewm.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@TestComponent
@RequiredArgsConstructor
public class TestData {
    private final EwmServiceApi serviceApi;
    private final ObjectMapper mapper;
    private final Faker faker = new Faker(Locale.of("RU"));


    public CategoryDto randomCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, faker.random().hex(32));
        return parse(serviceApi.createCategory(categoryDto).andReturn(), CategoryDto.class);
    }

    public UserDto randomUser() throws Exception {
        UserDto userDto = new UserDto(null, faker.name().name(), faker.internet().emailAddress());
        return parse(serviceApi.createUser(userDto).andReturn(), UserDto.class);
    }

    public EventFullDto randomEvent(UserDto user, CategoryDto category) throws Exception {
        NewEventDto eventDto = new NewEventDtoBuilder(category.id()).build();
        return parse(serviceApi.createUserEvent(user.id(), eventDto).andReturn(), EventFullDto.class);
    }

    public void publishEvent(long eventId) throws Exception {
        UpdateEventDto updateEventDto = new UpdateEventDto(null, null, null,
                null, null, null, null, null,
                null, EventStateAction.PUBLISH_EVENT
        );
        serviceApi.updateAdminEvent(eventId, updateEventDto);
    }

    public static String capitalize(String value) {
        value = value.trim();
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    private <T> T parse(MvcResult result, Class<T> cls) throws Exception {
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return mapper.readValue(content, cls);
    }
}
