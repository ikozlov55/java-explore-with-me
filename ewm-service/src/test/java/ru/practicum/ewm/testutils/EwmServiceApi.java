package ru.practicum.ewm.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.EventsSort;
import ru.practicum.ewm.request.dto.RequestStatusUpdateRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestComponent
@RequiredArgsConstructor
public class EwmServiceApi {
    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    public ResultActions getCategory(long categoryId) throws Exception {
        return mockMvc.perform(get("/categories/{categoryId}", categoryId));
    }

    public ResultActions getCategories(int from, int size) throws Exception {
        return mockMvc.perform(get("/categories?from={from}&size={size}", from, size));
    }

    public ResultActions createCategory(CategoryDto categoryDto) throws Exception {
        return mockMvc.perform(post("/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDto)));
    }

    public ResultActions deleteCategory(long categoryId) throws Exception {
        return mockMvc.perform(delete("/admin/categories/{categoryId}", categoryId));
    }

    public ResultActions updateCategory(long categoryId, CategoryDto categoryDto) throws Exception {
        return mockMvc.perform(patch("/admin/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(categoryDto)));
    }

    public ResultActions getUsers(List<Long> ids, int from, int size) throws Exception {
        return mockMvc.perform(get("/admin/users?ids={ids}&from={from}&size={size}",
                ids.toArray(), from, size));
    }

    public ResultActions createUser(UserDto userDto) throws Exception {
        return mockMvc.perform(post("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto)));
    }

    public ResultActions deleteUser(long userId) throws Exception {
        return mockMvc.perform(delete("/admin/users/{userId}", userId));
    }

    public ResultActions getUserEvents(long userId, int from, int size) throws Exception {
        return mockMvc.perform(get("/users/{userId}/events?from={from}&size={size}",
                userId, from, size));
    }

    public ResultActions createUserEvent(long userId, NewEventDto eventDto) throws Exception {
        return mockMvc.perform(post("/users/{userId}/events", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)));
    }

    public ResultActions getUserEvent(long userId, long eventId) throws Exception {
        return mockMvc.perform(get("/users/{userId}/events/{eventId}", userId, eventId));
    }

    public ResultActions updateUserEvent(long userId, long eventId, UpdateEventDto eventDto) throws Exception {
        return mockMvc.perform(patch("/users/{userId}/events/{eventId}", userId, eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)));
    }

    public ResultActions getAdminEvents(List<Long> users, List<EventState> states,
                                        List<Long> categories, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, int from, int size) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/admin/events")
                .queryParam("users", users)
                .queryParam("states", states)
                .queryParam("categories", categories)
                .queryParam("rangeStart", rangeStart)
                .queryParam("rangeEnd", rangeEnd)
                .queryParam("from", from)
                .queryParam("size", size);
        return mockMvc.perform(get(builder.toUriString()));
    }

    public ResultActions updateAdminEvent(long eventId, UpdateEventDto eventDto) throws Exception {
        return mockMvc.perform(patch("/admin/events/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)));
    }

    public ResultActions getPublicEvents(String text, List<Long> categories,
                                         Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         boolean onlyAvailable, EventsSort sort, int from, int size) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/events")
                .queryParam("text", text)
                .queryParam("categories", categories)
                .queryParam("paid", paid)
                .queryParam("rangeStart", rangeStart)
                .queryParam("rangeEnd", rangeEnd)
                .queryParam("onlyAvailable", onlyAvailable)
                .queryParam("sort", sort)
                .queryParam("from", from)
                .queryParam("size", size);
        return mockMvc.perform(get(builder.toUriString()));
    }

    public ResultActions getPublicEvent(long eventId) throws Exception {
        return mockMvc.perform(get("/events/{eventId}", eventId));
    }

    public ResultActions getUserEventRequests(long userId, long eventId) throws Exception {
        return mockMvc.perform(get("/user/{userId}/events/{eventId}/requests", userId, eventId));
    }

    public ResultActions updateUserEventRequests(long userId, long eventId,
                                                 RequestStatusUpdateRequest statusUpdateRequest) throws Exception {
        return mockMvc.perform(patch("/user/{userId}/events/{eventId}/requests", userId, eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(statusUpdateRequest)));
    }

    public ResultActions getRequests(long userId) throws Exception {
        return mockMvc.perform(get("/user/{userId}/requests", userId));

    }

    public ResultActions createRequest(long userId, long eventId) throws Exception {
        return mockMvc.perform(post("/user/{userId}/requests?eventId={eventId}", userId, eventId));
    }

    public ResultActions cancelRequest(long userId, long requestId) throws Exception {
        return mockMvc.perform(patch("/user/{userId}/requests/{requestId}/cancel", userId, requestId));
    }
}
